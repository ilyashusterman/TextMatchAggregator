package org.example;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
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
     * Aggregates multiple name-location maps into a single consolidated map using a concurrent collector.
     * @param chunkResults List of maps from name to list of [lineOffset, charOffset] pairs
     * @return Consolidated map from name to combined list of all locations
     */
    public static Map<String, List<int[]>> aggregate(List<Map<String, List<int[]>>> chunkResults) {
        return chunkResults.parallelStream()
            .flatMap(nameLocationMap -> nameLocationMap.entrySet().stream())
            .collect(Collectors.toConcurrentMap(
                Map.Entry::getKey,
                entry -> entry.getValue(),
                (list1, list2) -> {
                    List<int[]> merged = new java.util.ArrayList<>(list1.size() + list2.size());
                    merged.addAll(list1);
                    merged.addAll(list2);
                    return merged;
                },
                ConcurrentHashMap::new
            ));
    }
}