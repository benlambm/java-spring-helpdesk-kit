package edu.vwcc.itp220mod4;

/**
 * One row from the help desk ticket file.
 *
 * A blank technician means the ticket has not been assigned yet.
 */
public record Ticket(
        String id,
        String category,
        Priority priority,
        TicketStatus status,
        String technician) {
}
