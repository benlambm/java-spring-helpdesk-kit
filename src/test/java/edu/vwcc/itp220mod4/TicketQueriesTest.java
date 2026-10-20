package edu.vwcc.itp220mod4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

/** Instructions: Use these tests to check both TicketQueries methods. */
class TicketQueriesTest {

    @Test
    void openTicketsKeepsOnlyOpenTickets() {
        Ticket open = ticket("HD-101", TicketStatus.OPEN, "Maya");
        Ticket closed = ticket("HD-102", TicketStatus.CLOSED, "Jordan");

        assertEquals(List.of(open),
                TicketQueries.openTickets(List.of(open, closed)));
    }

    @Test
    void openTicketsPreservesOriginalOrder() {
        Ticket first = ticket("HD-101", TicketStatus.OPEN, "Maya");
        Ticket closed = ticket("HD-102", TicketStatus.CLOSED, "Jordan");
        Ticket third = ticket("HD-103", TicketStatus.OPEN, "Alex");

        assertEquals(List.of(first, third),
                TicketQueries.openTickets(List.of(first, closed, third)));
    }

    @Test
    void openTicketsReturnsAnEmptyListWhenEverythingIsClosed() {
        Ticket closed = ticket("HD-102", TicketStatus.CLOSED, "Jordan");

        assertEquals(List.of(), TicketQueries.openTickets(List.of(closed)));
    }

    @Test
    void technicianNamesRemovesDuplicates() {
        List<Ticket> tickets = List.of(
                ticket("HD-101", TicketStatus.OPEN, "Maya"),
                ticket("HD-102", TicketStatus.CLOSED, "Jordan"),
                ticket("HD-103", TicketStatus.OPEN, "Maya"));

        assertEquals(Set.of("Maya", "Jordan"),
                TicketQueries.technicianNames(tickets));
    }

    @Test
    void technicianNamesIgnoresBlankNames() {
        List<Ticket> tickets = List.of(
                ticket("HD-101", TicketStatus.OPEN, "Maya"),
                ticket("HD-102", TicketStatus.OPEN, ""),
                ticket("HD-103", TicketStatus.OPEN, "   "));

        assertEquals(Set.of("Maya"),
                TicketQueries.technicianNames(tickets));
    }

    private Ticket ticket(String id, TicketStatus status, String technician) {
        return new Ticket(id, "Network", Priority.MEDIUM, status, technician);
    }
}
