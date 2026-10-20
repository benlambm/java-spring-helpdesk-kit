package edu.vwcc.itp220mod4;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Loads a CSV file of tickets into the database. */
@Service
public class TicketImporter {

    private final TicketRepository repository;

    public TicketImporter(TicketRepository repository) {
        this.repository = repository;
    }

    /**
     * Instructions:
     * Replace everything in the tickets table with the tickets in csvPath.
     * Read the file with your Coding Homework 3 TicketCsvReader, delete the
     * old rows, save every ticket, and return how many you saved.
     * Keep @Transactional: it makes the whole import one unit, so if any save
     * fails, the delete is rolled back too and the old rows survive.
     */
    @Transactional
    public int importCsv(Path csvPath) throws IOException {
        // TODO: your code goes here
        throw new UnsupportedOperationException("TODO");
    }
}
