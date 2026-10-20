package edu.vwcc.itp220mod4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

/** Instructions: Use these tests to check TicketStats.countByCategory(). */
class TicketStatsTest {

    @Test
    void countsOneTicketInItsCategory() {
        List<Ticket> tickets = List.of(ticket("HD-101", "Network"));

        assertEquals(Map.of("Network", 1),
                TicketStats.countByCategory(tickets));
    }

    @Test
    void countsRepeatedCategories() {
        List<Ticket> tickets = List.of(
                ticket("HD-101", "Network"),
                ticket("HD-102", "Network"),
                ticket("HD-103", "Network"));

        assertEquals(Map.of("Network", 3),
                TicketStats.countByCategory(tickets));
    }

    @Test
    void countsSeveralCategoriesIndependently() {
        List<Ticket> tickets = List.of(
                ticket("HD-101", "Network"),
                ticket("HD-102", "Hardware"),
                ticket("HD-103", "Network"),
                ticket("HD-104", "Software"));

        Map<String, Integer> expected = Map.of(
                "Network", 2,
                "Hardware", 1,
                "Software", 1);

        assertEquals(expected, TicketStats.countByCategory(tickets));
    }

    @Test
    void returnsAnEmptyMapForNoTickets() {
        assertEquals(Map.of(), TicketStats.countByCategory(List.of()));
    }

    private Ticket ticket(String id, String category) {
        return new Ticket(
                id, category, Priority.MEDIUM, TicketStatus.OPEN, "Maya");
    }
}
