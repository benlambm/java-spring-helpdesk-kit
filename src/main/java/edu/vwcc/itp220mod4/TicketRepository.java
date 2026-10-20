package edu.vwcc.itp220mod4;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

    /**
 * The help desk's tickets, stored in the tickets table (see schema.sql).
 * Every method talks to the database through Spring's JdbcClient.
 */
@Repository
public class TicketRepository {

    private final JdbcClient jdbc;

    // Spring passes in a ready-made JdbcClient. No connections to open or close.
    public TicketRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    /** Provided: turns one database row into a Ticket. Reuse it in your queries. */
    static Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Ticket(
                rs.getString("id"),
                rs.getString("category"),
                Priority.valueOf(rs.getString("priority")),
                TicketStatus.valueOf(rs.getString("status")),
                rs.getString("technician"));
    }

    /** Worked example: count the rows. Copy this shape: sql, then query, then a result. */
    public int count() {
        return jdbc.sql("SELECT COUNT(*) FROM tickets")
                .query(Integer.class)
                .single();
    }

    /**
     * Instructions:
     * Insert one ticket. Pass every value as a ? parameter, never by gluing it
     * into the SQL string. Store priority and status by their enum names.
     */
    public void save(Ticket ticket) {
        // TODO: your code goes here
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Instructions:
     * Delete every row in the tickets table.
     */
    public void deleteAll() {
        // TODO: your code goes here
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Instructions:
     * Return the ticket with this id, or Optional.empty() if there is none.
     * Hint: query(TicketRepository::mapRow), then optional().
     */
    public Optional<Ticket> findById(String id) {
        // TODO: your code goes here
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Instructions:
     * Return every OPEN ticket, ordered by id. Coding Homework 3 did this with
     * a loop; here the database does the filtering and sorting.
     */
    public List<Ticket> findOpen() {
        // TODO: your code goes here
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Instructions:
     * Return each assigned technician name once, ignoring blank names.
     * Hint: SELECT DISTINCT, and query(String.class) for a one-column result.
     */
    public Set<String> findTechnicianNames() {
        // TODO: your code goes here
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Instructions:
     * Return a Map from each category to its number of tickets. Let SQL count
     * with GROUP BY, then copy each result row into the Map.
     */
    public Map<String, Integer> countByCategory() {
        // TODO: your code goes here
        throw new UnsupportedOperationException("TODO");
    }

}
