package org.example;

import java.util.List;
import java.util.Map;

public final class Printer {
    private static final String LOG_PREFIX = "[printer]";
    private static final String NAME_LOCATION_SEPARATOR = " --> ";
    private static final String LOCATION_FORMAT = "[lineOffset=%d, charOffset=%d]";
    private static final String LOCATION_SEPARATOR = ", ";

    private Printer() {
        // Utility class - prevent instantiation
    }

    /**
     * Prints the aggregated results in a readable format using a functional stream pipeline.
     * @param nameLocations The merged map from name to list of locations
     */
    public static void printResults(Map<String, List<int[]>> nameLocations) {
        logStartOfPrinting(nameLocations.size());
        nameLocations.entrySet().parallelStream()
            .map(Printer::formatNameWithLocations)
            .forEach(Printer::printLine);
    }

    private static void logStartOfPrinting(int nameCount) {
        log("Printing results asynchronously...");
        log("Printing %d names.", nameCount);
    }

    public static String formatNameWithLocations(Map.Entry<String, List<int[]>> entry) {
        String name = entry.getKey();
        List<int[]> locations = entry.getValue();
        String locationsStr = formatLocations(locations);
        return name + NAME_LOCATION_SEPARATOR + locationsStr;
    }

    private static String formatLocations(List<int[]> locations) {
        return locations.stream()
                .map(loc -> String.format(LOCATION_FORMAT, loc[0], loc[1]))
                .reduce((a, b) -> a + LOCATION_SEPARATOR + b)
                .map(s -> "[" + s + "]")
                .orElse("[]");
    }

    private static void printLine(String line) {
        log(line);
    }

    private static void log(String message, Object... args) {
        String formattedMessage = args.length > 0 ? String.format(message, args) : message;
        System.out.printf("%s %s%n", LOG_PREFIX, formattedMessage);
    }
} 