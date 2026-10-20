package edu.vwcc.itp220mod4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Coding Homework 3's Main, reborn. On startup it loads the CSV into the
 * database, prints the same summary, and writes the same report. Provided.
 */
@Component
public class HelpDeskRunner implements CommandLineRunner {

    private final TicketImporter importer;
    private final TicketRepository repository;
    private final Path csvFile;
    private final Path reportFile;

    public HelpDeskRunner(
            TicketImporter importer,
            TicketRepository repository,
            @Value("${helpdesk.csv-file}") String csvFile,
            @Value("${helpdesk.report-file}") String reportFile) {
        this.importer = importer;
        this.repository = repository;
        this.csvFile = Path.of(csvFile);
        this.reportFile = Path.of(reportFile);
    }

    @Override
    public void run(String... args) throws IOException {
        int loaded = importer.importCsv(csvFile);

        Map<String, Integer> categoryCounts = repository.countByCategory();
        Files.createDirectories(reportFile.toAbsolutePath().getParent());
        TicketReportWriter.writeCategorySummary(reportFile, categoryCounts);

        System.out.println("Tickets loaded: " + loaded);
        System.out.println("Open tickets: " + repository.findOpen().size());
        System.out.println("Assigned technicians: " + repository.findTechnicianNames().size());
        System.out.println("Summary written to: " + reportFile);
    }
}
