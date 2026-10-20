package edu.vwcc.itp220mod4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Instructions: Use these tests to check TicketCsvReader.read(). */
class TicketCsvReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void readsEveryDataRow() throws IOException {
        Path csv = writeCsv(
                "id,category,priority,status,technician",
                "HD-101,Network,HIGH,OPEN,Maya",
                "HD-102,Password,LOW,CLOSED,Jordan");

        List<Ticket> tickets = TicketCsvReader.read(csv);

        assertEquals(2, tickets.size());
    }

    @Test
    void convertsAllFiveColumnsToTicketFields() throws IOException {
        Path csv = writeCsv(
                "id,category,priority,status,technician",
                "HD-103,Hardware,MEDIUM,OPEN,Alex");

        Ticket ticket = TicketCsvReader.read(csv).get(0);

        assertEquals("HD-103", ticket.id());
        assertEquals("Hardware", ticket.category());
        assertEquals(Priority.MEDIUM, ticket.priority());
        assertEquals(TicketStatus.OPEN, ticket.status());
        assertEquals("Alex", ticket.technician());
    }

    @Test
    void preservesAnEmptyTechnicianField() throws IOException {
        Path csv = writeCsv(
                "id,category,priority,status,technician",
                "HD-104,Software,HIGH,OPEN,");

        Ticket ticket = TicketCsvReader.read(csv).get(0);

        assertEquals("", ticket.technician());
    }

    @Test
    void skipsBlankLines() throws IOException {
        Path csv = writeCsv(
                "id,category,priority,status,technician",
                "HD-101,Network,HIGH,OPEN,Maya",
                "",
                "   ",
                "HD-102,Password,LOW,CLOSED,Jordan");

        assertEquals(2, TicketCsvReader.read(csv).size());
    }

    @Test
    void rejectsARowWithTheWrongNumberOfColumns() throws IOException {
        Path csv = writeCsv(
                "id,category,priority,status,technician",
                "HD-105,Network,HIGH,OPEN");

        assertThrows(IllegalArgumentException.class,
                () -> TicketCsvReader.read(csv));
    }

    private Path writeCsv(String... lines) throws IOException {
        Path csv = tempDir.resolve("tickets.csv");
        Files.write(csv, List.of(lines));
        return csv;
    }
}
