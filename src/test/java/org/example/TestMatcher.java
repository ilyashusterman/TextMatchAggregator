package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TestMatcher {
    @Test
    void testMatcherFindsNamesAndOffsets() {
        String text = "James and John went to see Robert. James was happy.";
        int lineOffset = 10;
        FileReader.Chunk chunk = new FileReader.Chunk(text, lineOffset);
        List<String> names = Arrays.asList("James", "John", "Robert", "Michael");
        Map<String, List<int[]>> results = Matcher.matcher(chunk, names);
        assertTrue(results.containsKey("James"));
        assertTrue(results.containsKey("John"));
        assertTrue(results.containsKey("Robert"));
        assertFalse(results.containsKey("Michael"));
        // Check offsets for James
        List<int[]> jamesOffsets = results.get("James");
        assertEquals(2, jamesOffsets.size());
        assertEquals(lineOffset, jamesOffsets.get(0)[0]);
        assertEquals(text.indexOf("James"), jamesOffsets.get(0)[1]);
        assertEquals(lineOffset, jamesOffsets.get(1)[0]);
        assertEquals(text.lastIndexOf("James"), jamesOffsets.get(1)[1]);
        // Check offset for John
        List<int[]> johnOffsets = results.get("John");
        assertEquals(1, johnOffsets.size());
        assertEquals(lineOffset, johnOffsets.get(0)[0]);
        assertEquals(text.indexOf("John"), johnOffsets.get(0)[1]);
    }
} 