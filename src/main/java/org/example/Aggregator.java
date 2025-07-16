package org.example;

import java.util.*;

public class Aggregator {
    /**
     * Aggregates a list of name-location maps into a single merged map.
     * @param results List of maps from name to list of [lineOffset, charOffset] pairs
     * @return Merged map from name to combined list of locations
     */
    public static Map<String, List<int[]>> aggregate(List<Map<String, List<int[]>>> results) {
        Map<String, List<int[]>> merged = new HashMap<>();
        for (Map<String, List<int[]>> res : results) {
            for (Map.Entry<String, List<int[]>> entry : res.entrySet()) {
                merged.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).addAll(entry.getValue());
            }
        }
        return merged;
    }
}