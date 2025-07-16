package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class FileReader {
    private static final int DEFAULT_LINES_PER_CHUNK = 1000;
    // private static final Pattern SANITIZE_PATTERN = Pattern.compile("(```[\\s\\S]*?```)|def\\s+\\w+\\s*\\([^)]*\\)\\s*:|function\\s+\\w+\\s*\\([^)]*\\)\\s*\\{");

    public static class Chunk {
        private final String text;
        private final int lineOffset;

        public Chunk(String text, int lineOffset) {
            this.text = text;
            this.lineOffset = lineOffset;
        }

        public String getText() {
            return text;
        }

        public int getLineOffset() {
            return lineOffset;
        }
    }

    public static List<Chunk> readText(Path filePath) throws IOException {
        return readText(filePath, DEFAULT_LINES_PER_CHUNK);
    }

    public static List<Chunk> readText(Path filePath, int linesPerChunk) throws IOException {
        List<Chunk> chunks = new ArrayList<>();
        List<String> chunkLines = new ArrayList<>();
        int startLine = 0;
        int totalLines = 0;
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                // chunkLines.add(sanitizeText(line));
                chunkLines.add(line);
                totalLines++;
                if (chunkLines.size() == linesPerChunk) {
                    chunks.add(new Chunk(String.join("", chunkLines), startLine));
                    startLine = totalLines;
                    chunkLines.clear();
                }
            }
            if (!chunkLines.isEmpty()) {
                chunks.add(new Chunk(String.join("", chunkLines), startLine));
            }
        }
        return chunks;
    }

//    private static String sanitizeText(String text) {
//        return SANITIZE_PATTERN.matcher(text).replaceAll("").trim();
//    }
} 