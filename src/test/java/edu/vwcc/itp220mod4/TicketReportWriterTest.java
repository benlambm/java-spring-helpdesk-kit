package edu.vwcc.itp220mod4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Instructions: Use these tests to check
 * TicketReportWriter.writeCategorySummary().
 */
class TicketReportWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void createsTheOutputFile() throws IOException {
        Path output = tempDir.resolve("summary.txt");

        TicketReportWriter.writeCategorySummary(
                output, Map.of("Network", 2));

        assertTrue(Files.exists(output));
    }

    @Test
    void writesCategoriesInAlphabeticalOrder() throws IOException {
        Path output = tempDir.resolve("summary.txt");
        Map<String, Integer> counts = Map.of(
                "Software", 1,
                "Network", 3,
                "Hardware", 2);

        TicketReportWriter.writeCategorySummary(output, counts);

        assertEquals(List.of(
                "Hardware: 2",
                "Network: 3",
                "Software: 1"), Files.readAllLines(output));
    }

    @Test
    void usesTheExactCategoryColonCountFormat() throws IOException {
        Path output = tempDir.resolve("summary.txt");

        TicketReportWriter.writeCategorySummary(
                output, Map.of("Password", 4));

        assertEquals(List.of("Password: 4"), Files.readAllLines(output));
    }

    @Test
    void writesAnEmptyFileForAnEmptyMap() throws IOException {
        Path output = tempDir.resolve("summary.txt");

        TicketReportWriter.writeCategorySummary(output, Map.of());

        assertEquals(List.of(), Files.readAllLines(output));
    }

    @Test
    void replacesExistingFileContents() throws IOException {
        Path output = tempDir.resolve("summary.txt");
        Files.write(output, List.of("old content", "more old content"));

        TicketReportWriter.writeCategorySummary(
                output, Map.of("Hardware", 1));

        assertEquals(List.of("Hardware: 1"), Files.readAllLines(output));
    }
}
