package org.example;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    // List of names to search for
    private static final List<String> NAMES = List.of(
"James", "John", "Robert", "Michael", "William", "David", "Richard", "Charles", 
"Joseph", "Thomas", "Christopher", "Daniel", "Paul", "Mark", "Donald", "George",
 "Kenneth", "Steven", "Edward", "Brian", "Ronald", "Anthony", "Kevin", "Jason",
  "Matthew", "Gary", "Timothy", "Jose", "Larry", "Jeffrey", "Frank", "Scott", 
  "Eric", "Stephen", "Andrew", "Raymond", "Gregory", "Joshua", "Jerry", "Dennis",
   "Walter", "Patrick", "Peter", "Harold", "Douglas", "Henry", "Carl", "Arthur", "Ryan", "Roger"
    );

    private static void log(String message, Object... args) {
        System.out.printf("[main] %s%n", args.length > 0 ? String.format(message, args) : message);
    }
    private static void logError(String message, Object... args) {
        System.err.printf("[main] ERROR: %s%n", args.length > 0 ? String.format(message, args) : message);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            logError("Usage: java -cp <jar> org.example.Main <inputFile>");
            System.exit(1);
        }
        String inputFile = args[0];
        log("main() called with inputFile: %s", inputFile);
        long benchmarkStart = System.currentTimeMillis();
        try {
            log("Starting main logic");
            log("Reading file: %s", inputFile);
            List<FileReader.Chunk> chunks = FileReader.readText(Path.of(inputFile));
            log("File split into %d chunks.", chunks.size());
            log("Spawning worker threads for each chunk...");
            long mainPid = ProcessHandle.current().pid();
            // Combine map and reduce: process chunks in parallel, collect results, and aggregate in one stream pipeline
            Map<String, List<int[]>> merged = Aggregator.aggregate(
                chunks.parallelStream()
                    .map(chunk -> {
                        int workerId = chunks.indexOf(chunk);
                        long pid = ProcessHandle.current().pid();
                        log("Worker %d (PID %d) started (main PID %d).", workerId, pid, mainPid);
                        Map<String, List<int[]>> result = MatcherUtil.matcher(chunk, NAMES);
                        log("Worker %d (PID %d) finished.", workerId, pid);
                        return result;
                    })
                    .toList()
            );
            log("All workers finished. Aggregating results...");
            log("\nWaiting for results...");
            log("\n==================== RESULTS ====================\n");
            Printer.printResults(merged);
            log("\n==================== END RESULTS ====================\n");
            log("Results printed.");
            long benchmarkEnd = System.currentTimeMillis();
            log("BENCHMARK: Total execution time: %d ms", (benchmarkEnd - benchmarkStart));
        } catch (Exception e) {
            logError("Error in main: %s", e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }
}