package org.example;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TestPrinter {
    @Test
    void testPrintResultsRuns() {
        Map<String, List<int[]>> merged = new HashMap<>();
        merged.put("Alice", List.of(new int[]{0, 1}, new int[]{2, 3}));
        merged.put("Bob", List.of(new int[]{1, 4}));
        Printer.printResults(merged);
    }
} 