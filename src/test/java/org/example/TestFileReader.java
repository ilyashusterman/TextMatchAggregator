package org.example;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestFileReader {
    @Test
    void testReadTextChunksAndSanitization() throws Exception {
        Path filePath = Path.of("src/test/resources/test-small.txt");
        List<FileReader.Chunk> chunks = FileReader.readText(filePath, 2); // Small chunk size for test
        assertFalse(chunks.isEmpty(), "Chunks should not be empty");
        assertEquals(4, chunks.size(), "Should split into 4 chunks for 7 lines with chunk size 2");
        assertEquals(0, chunks.get(0).getLineOffset());
        assertEquals(2, chunks.get(1).getLineOffset());
        assertEquals(4, chunks.get(2).getLineOffset());
        // Check sanitization (no code blocks or function/def signatures)
        for (FileReader.Chunk chunk : chunks) {
            assertFalse(chunk.getText().contains("function"));
            assertFalse(chunk.getText().contains("def "));
            assertFalse(chunk.getText().contains("```"));
        }
    }
} 