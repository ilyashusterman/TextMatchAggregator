package org.example;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main application class for text matching and aggregation.
 * Processes large text files by splitting them into chunks and searching for names in parallel.
 */
public final class Main {
    private static final String LOG_PREFIX = "[main]";
    private static final String ERROR_PREFIX = "[main] ERROR:";
    private static final String USAGE_MESSAGE = "Usage: java -cp <jar> org.example.Main <inputFile>";
    private static final String RESULTS_HEADER = "==================== RESULTS ====================";
    private static final String RESULTS_FOOTER = "==================== END RESULTS ====================";

    private static final List<String> NAMES_TO_SEARCH = List.of(
        "James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles",
        "Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George",
        "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason",
        "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott",
        "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis",
        "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"
    );

    private Main() {
        // Utility class - prevent instantiation
    }

    public static void main(String[] args) {
        try {
            validateArgsOrExit(args);
            String inputFilePath = args[0];
            long startTime = System.currentTimeMillis();
            log("main() called with inputFile: %s", inputFilePath);
            // Explicitly initialize the matcher pattern for all chunks
            Matcher.initializePattern(NAMES_TO_SEARCH);

            // Map-Reduce: read, chunk, process, aggregate, and print in a single pipeline
            log("\nWaiting for results...");
            log("\n%s\n", RESULTS_HEADER);

            FileReader.readTextStream(Path.of(inputFilePath))
                .peek(chunk -> log("Chunk created at line offset %d", chunk.getLineOffset()))
                .map(chunk -> ChunkProcessor.processChunk(chunk, NAMES_TO_SEARCH))
                .collect(Collectors.collectingAndThen(
                    Collectors.toList(),
                    Aggregator::aggregate
                ))
                .entrySet()
                .parallelStream()
                .map(Printer::formatNameWithLocations)
                .forEach(line -> System.out.printf("%s %s%n", "[printer]", line));

            log("\n%s\n", RESULTS_FOOTER);
            log("Results printed.");
            logBenchmarkResults(startTime);
        } catch (Exception e) {
            logError("Error in main: %s", e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }

    private static void validateArgsOrExit(String[] args) {
        if (args.length < 1) {
            logError(USAGE_MESSAGE);
            System.exit(1);
        }
    }

    private static void logBenchmarkResults(long startTime) {
        long endTime = System.currentTimeMillis();
        long totalExecutionTime = endTime - startTime;
        log("BENCHMARK: Total execution time: %d ms", totalExecutionTime);
    }

    private static void log(String message, Object... args) {
        String formattedMessage = args.length > 0 ? String.format(message, args) : message;
        System.out.printf("%s %s%n", LOG_PREFIX, formattedMessage);
    }

    private static void logError(String message, Object... args) {
        String formattedMessage = args.length > 0 ? String.format(message, args) : message;
        System.err.printf("%s %s%n", ERROR_PREFIX, formattedMessage);
    }
} 
