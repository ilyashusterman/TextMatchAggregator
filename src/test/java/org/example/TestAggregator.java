package org.example;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TestAggregator {
    @Test
    void testAggregateMergesResults() {
        Map<String, List<int[]>> res1 = new HashMap<>();
        res1.put("James", List.of(new int[]{0, 1}, new int[]{0, 10}));
        res1.put("John", List.of(new int[]{0, 5}));
        Map<String, List<int[]>> res2 = new HashMap<>();
        res2.put("James", List.of(new int[]{2, 3}));
        res2.put("Robert", List.of(new int[]{2, 7}));
        List<Map<String, List<int[]>>> results = List.of(res1, res2);
        Map<String, List<int[]>> merged = Aggregator.aggregate(results);
        assertEquals(3, merged.size());
        assertTrue(merged.containsKey("James"));
        assertTrue(merged.containsKey("John"));
        assertTrue(merged.containsKey("Robert"));
        assertEquals(3, merged.get("James").size());
        assertEquals(1, merged.get("John").size());
        assertEquals(1, merged.get("Robert").size());
        // Optionally print results for visual check
        Printer.printResults(merged);
    }
}