package org.example;

import java.util.List;
import java.util.Map;

/**
 * Utility class for processing a single chunk in the map step of the pipeline.
 */
public final class ChunkProcessor {
    private ChunkProcessor() {
        // Utility class - prevent instantiation
    }

    public static Map<String, List<int[]>> processChunk(FileReader.Chunk chunk, List<String> names) {
        long mainProcessId = ProcessHandle.current().pid();
        int workerId = chunk.getLineOffset(); // Use line offset as a unique id for demonstration
        long workerProcessId = ProcessHandle.current().pid();
        log("Worker %d (PID %d) started (main PID %d).", workerId, workerProcessId, mainProcessId);
        Map<String, List<int[]>> result = Matcher.matcher(chunk, names);
        log("Worker %d (PID %d) finished.", workerId, workerProcessId);
        return result;
    }

    private static void log(String message, Object... args) {
        String formattedMessage = args.length > 0 ? String.format(message, args) : message;
        System.out.printf("[chunk] %s%n", formattedMessage);
    }
} 