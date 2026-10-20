package edu.vwcc.itp220mod4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Exercise 1: read help desk tickets from a CSV file.
 */
public class TicketCsvReader {

    /**
     * Instructions:
     * Read all lines from csvPath. The first line is the header, so skip it.
     * Skip blank lines. Split every other line into exactly five fields using
     * split(",", -1), trim each field, and build a Ticket. Convert priority
     * and status with their enum valueOf methods. If a data row does not have
     * exactly five fields, throw IllegalArgumentException. Return the tickets
     * in the same order as the file.
     */
    public static List<Ticket> read(Path csvPath) throws IOException {
        // TODO: your code goes here
        throw new UnsupportedOperationException("TODO");
    }
}
