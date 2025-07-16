package org.example;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class Printer {
    private static void log(String message, Object... args) {
        System.out.printf("[printer] %s%n", args.length > 0 ? String.format(message, args) : message);
    }

    /**
     * Prints the aggregated results in a readable format, concurrently, using map-reduce style.
     * @param merged The merged map from name to list of locations
     */
    public static void printResults(Map<String, List<int[]>> merged) {
        log("Printing results asynchronously...");
        log("Printing %d names.", merged.size());
        // Combine map and reduce: submit print tasks and wait for all in one stream pipeline
        merged.keySet().parallelStream()
            .map(name -> {
                List<int[]> locs = merged.get(name);
                StringBuilder sb = new StringBuilder();
                sb.append(name).append(" --> [");
                for (int i = 0; i < locs.size(); i++) {
                    int[] loc = locs.get(i) ;
                    sb.append(String.format("[lineOffset=%d, charOffset=%d]", loc[0], loc[1]));
                    if (i < locs.size() - 1) sb.append(", ");
                }
                sb.append("]");
                log(sb.toString());
                return sb;
            }).toList();
    }
} 