package edu.vwcc.itp220mod4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Instructions: these tests are the specification for TicketImporter.
 *
 * Unlike TicketRepositoryTest, these tests switch off the automatic test
 * rollback (NOT_SUPPORTED), so the importer's own @Transactional is what
 * decides whether changes stick. That is the whole point of the last test.
 */
@JdbcTest
@Import({TicketRepository.class, TicketImporter.class})
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class TicketImporterTest {

    private static final String HEADER = "id,category,priority,status,technician";

    @Autowired
    TicketImporter importer;

    @Autowired
    JdbcClient jdbc;

    @TempDir
    Path tempDir;

    @BeforeEach
    void startWithAnEmptyTable() {
        jdbc.sql("DELETE FROM tickets").update();
    }

    @Test
    void importsEveryRowAndReturnsTheCount() throws IOException {
        Path csv = writeCsv(HEADER,
                "HD-101,Network,HIGH,OPEN,Maya",
                "HD-102,Password,LOW,CLOSED,Jordan",
                "HD-103,Hardware,MEDIUM,OPEN,");

        int saved = importer.importCsv(csv);

        assertEquals(3, saved);
        assertEquals(List.of("HD-101", "HD-102", "HD-103"), ids());
    }

    @Test
    void importReplacesTheRowsThatWereThereBefore() throws IOException {
        jdbc.sql("INSERT INTO tickets VALUES ('OLD-1', 'Network', 'LOW', 'OPEN', 'Maya')").update();
        Path csv = writeCsv(HEADER, "HD-101,Network,HIGH,OPEN,Maya");

        importer.importCsv(csv);

        assertEquals(List.of("HD-101"), ids());
    }

    @Test
    void aFailedImportRollsBackAndKeepsTheOldRows() throws IOException {
        jdbc.sql("INSERT INTO tickets VALUES ('OLD-1', 'Network', 'LOW', 'OPEN', 'Maya')").update();
        // The same id twice: the second save breaks the primary key.
        Path csv = writeCsv(HEADER,
                "HD-101,Network,HIGH,OPEN,Maya",
                "HD-101,Password,LOW,CLOSED,Jordan");

        assertThrows(DataIntegrityViolationException.class,
                () -> importer.importCsv(csv));

        // @Transactional undid the delete, so the old row is still there.
        assertEquals(List.of("OLD-1"), ids());
    }

    private Path writeCsv(String... lines) throws IOException {
        Path csv = tempDir.resolve("tickets.csv");
        Files.write(csv, List.of(lines));
        return csv;
    }

    private List<String> ids() {
        return jdbc.sql("SELECT id FROM tickets ORDER BY id").query(String.class).list();
    }
}
