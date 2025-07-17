package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Utility class for aggregating name-location maps from multiple processing chunks.
 * Combines results from parallel processing into a single consolidated map.
 */
public final class Aggregator {
    private Aggregator() {
        // Utility class - prevent instantiation
    }

    /**
     * Aggregates multiple name-location maps into a single consolidated map.
     * Uses functional programming style with streams for clean, readable aggregation.
     * 
     * @param chunkResults List of maps from name to list of [lineOffset, charOffset] pairs
     * @return Consolidated map from name to combined list of all locations
     */
    public static Map<String, List<int[]>> aggregate(List<Map<String, List<int[]>>> chunkResults) {
        return chunkResults.stream()
            .flatMap(nameLocationMap -> nameLocationMap.entrySet().stream())
            .collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.flatMapping(
                    entry -> entry.getValue().stream(),
                    Collectors.toList()
                )
            ));
    }
}