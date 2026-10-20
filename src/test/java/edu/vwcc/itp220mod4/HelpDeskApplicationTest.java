package edu.vwcc.itp220mod4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Instructions: the victory lap. This starts the whole application, exactly
 * as running HelpDeskApplication does, then checks what HelpDeskRunner did.
 */
@SpringBootTest(properties = "helpdesk.report-file=target/test-report/category-summary.txt")
class HelpDeskApplicationTest {

    @Autowired
    TicketRepository repository;

    @Test
    void startupLoadsTheSampleCsvAndWritesTheReport() throws IOException {
        assertEquals(8, repository.count());
        assertEquals(List.of(
                "Hardware: 2",
                "Network: 2",
                "Password: 2",
                "Software: 2"),
                Files.readAllLines(Path.of("target", "test-report", "category-summary.txt")));
    }
}
