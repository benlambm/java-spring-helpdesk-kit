package edu.vwcc.itp220mod4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;

/**
 * Instructions: these tests are the specification for TicketRepository.
 *
 * Each test gets a fresh, empty tickets table in an in-memory H2 database.
 * Rows a test needs are inserted with @Sql, and every change is rolled back
 * when the test ends, so the tests never affect each other.
 */
@JdbcTest
@Import(TicketRepository.class)
class TicketRepositoryTest {

    @Autowired
    TicketRepository repository;

    // The test's own way into the database, so one unfinished method
    // cannot break every test.
    @Autowired
    JdbcClient jdbc;

    @Test
    void saveStoresEveryField() {
        repository.save(new Ticket(
                "HD-101", "Network", Priority.HIGH, TicketStatus.OPEN, "Maya"));

        Map<String, Object> row = onlyRow();
        assertEquals("HD-101", row.get("id"));
        assertEquals("Network", row.get("category"));
        assertEquals("HIGH", row.get("priority"));
        assertEquals("OPEN", row.get("status"));
        assertEquals("Maya", row.get("technician"));
    }

    @Test
    void saveKeepsAnEmptyTechnician() {
        repository.save(new Ticket(
                "HD-103", "Hardware", Priority.MEDIUM, TicketStatus.OPEN, ""));

        assertEquals("", onlyRow().get("technician"));
    }

    @Test
    void saveTreatsTextAsDataNotSql() {
        String sneaky = "Network'); DROP TABLE tickets; --";

        repository.save(new Ticket(
                "HD-666", sneaky, Priority.LOW, TicketStatus.OPEN, "Maya"));

        // The table still exists, and the text was stored exactly as given.
        assertEquals(sneaky, onlyRow().get("category"));
    }

    @Test
    @Sql(statements = {
        "INSERT INTO tickets VALUES ('HD-101', 'Network', 'HIGH', 'OPEN', 'Maya')",
        "INSERT INTO tickets VALUES ('HD-102', 'Password', 'LOW', 'CLOSED', 'Jordan')"
    })
    void countReturnsTheNumberOfRows() {
        // count() is the worked example, so this test passes from day one.
        assertEquals(2, repository.count());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO tickets VALUES ('HD-101', 'Network', 'HIGH', 'OPEN', 'Maya')",
        "INSERT INTO tickets VALUES ('HD-102', 'Password', 'LOW', 'CLOSED', 'Jordan')"
    })
    void deleteAllEmptiesTheTable() {
        repository.deleteAll();

        assertEquals(0, rowCount());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO tickets VALUES ('HD-101', 'Network', 'HIGH', 'OPEN', 'Maya')",
        "INSERT INTO tickets VALUES ('HD-103', 'Hardware', 'MEDIUM', 'OPEN', '')"
    })
    void findByIdReturnsTheMatchingTicket() {
        Optional<Ticket> found = repository.findById("HD-103");

        assertEquals(Optional.of(new Ticket(
                "HD-103", "Hardware", Priority.MEDIUM, TicketStatus.OPEN, "")),
                found);
    }

    @Test
    @Sql(statements = "INSERT INTO tickets VALUES ('HD-101', 'Network', 'HIGH', 'OPEN', 'Maya')")
    void findByIdReturnsEmptyWhenTheIdIsMissing() {
        assertEquals(Optional.empty(), repository.findById("HD-999"));
    }

    @Test
    @Sql(statements = {
        "INSERT INTO tickets VALUES ('HD-103', 'Hardware', 'HIGH', 'OPEN', 'Maya')",
        "INSERT INTO tickets VALUES ('HD-102', 'Password', 'LOW', 'CLOSED', 'Jordan')",
        "INSERT INTO tickets VALUES ('HD-101', 'Network', 'HIGH', 'OPEN', 'Alex')"
    })
    void findOpenReturnsOnlyOpenTicketsOrderedById() {
        List<String> ids = idsOf(repository.findOpen());

        assertEquals(List.of("HD-101", "HD-103"), ids);
    }

    @Test
    @Sql(statements = "INSERT INTO tickets VALUES ('HD-102', 'Password', 'LOW', 'CLOSED', 'Jordan')")
    void findOpenReturnsAnEmptyListWhenEverythingIsClosed() {
        assertEquals(List.of(), repository.findOpen());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO tickets VALUES ('HD-101', 'Network', 'HIGH', 'OPEN', 'Maya')",
        "INSERT INTO tickets VALUES ('HD-102', 'Password', 'LOW', 'CLOSED', 'Jordan')",
        "INSERT INTO tickets VALUES ('HD-103', 'Hardware', 'MEDIUM', 'OPEN', 'Maya')",
        "INSERT INTO tickets VALUES ('HD-104', 'Software', 'MEDIUM', 'OPEN', '')",
        "INSERT INTO tickets VALUES ('HD-105', 'Software', 'LOW', 'OPEN', '   ')"
    })
    void findTechnicianNamesRemovesDuplicatesAndBlanks() {
        assertEquals(Set.of("Maya", "Jordan"), repository.findTechnicianNames());
    }

    @Test
    @Sql(statements = {
        "INSERT INTO tickets VALUES ('HD-101', 'Network', 'HIGH', 'OPEN', 'Maya')",
        "INSERT INTO tickets VALUES ('HD-102', 'Hardware', 'LOW', 'CLOSED', 'Jordan')",
        "INSERT INTO tickets VALUES ('HD-103', 'Network', 'MEDIUM', 'OPEN', 'Alex')"
    })
    void countByCategoryCountsEachCategory() {
        assertEquals(Map.of("Network", 2, "Hardware", 1),
                repository.countByCategory());
    }

    @Test
    void countByCategoryIsEmptyWhenThereAreNoTickets() {
        assertEquals(Map.of(), repository.countByCategory());
    }

    private Map<String, Object> onlyRow() {
        return jdbc.sql("SELECT * FROM tickets").query().singleRow();
    }

    private int rowCount() {
        return jdbc.sql("SELECT COUNT(*) FROM tickets").query(Integer.class).single();
    }

    private List<String> idsOf(List<Ticket> tickets) {
        return tickets.stream().map(Ticket::id).toList();
    }
}
