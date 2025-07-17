package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class for reading text files and splitting them into chunks.
 */
public final class FileReader {
    private static final int DEFAULT_LINES_PER_CHUNK = 1000;

    private FileReader() {
        // Utility class - prevent instantiation
    }

    /**
     * Represents a chunk of text from the file, along with its starting line offset.
     */
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

    /**
     * Reads the file and splits it into chunks of the default size, returning a parallel stream of chunks.
     * @param filePath Path to the file
     * @return Parallel stream of chunks
     * @throws IOException if file reading fails
     */
    public static Stream<Chunk> readTextStream(Path filePath) throws IOException {
        return readTextStream(filePath, DEFAULT_LINES_PER_CHUNK);
    }

    /**
     * Reads the file and splits it into chunks of the specified size, returning a parallel stream of chunks.
     * @param filePath Path to the file
     * @param linesPerChunk Number of lines per chunk
     * @return Parallel stream of chunks
     * @throws IOException if file reading fails
     */
    public static Stream<Chunk> readTextStream(Path filePath, int linesPerChunk) throws IOException {
        List<String> allLines;
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            allLines = reader.lines().collect(Collectors.toList());
        }
        int totalLines = allLines.size();
        int numChunks = (int) Math.ceil((double) totalLines / linesPerChunk);
        return IntStream.range(0, numChunks)
                .parallel()
                .mapToObj(chunkIndex -> createChunk(allLines, chunkIndex, linesPerChunk));
    }

    /**
     * Reads the file and splits it into chunks of the default size, returning a list (for compatibility).
     * @param filePath Path to the file
     * @return List of chunks
     * @throws IOException if file reading fails
     */
    public static List<Chunk> readText(Path filePath) throws IOException {
        return readText(filePath, DEFAULT_LINES_PER_CHUNK);
    }

    /**
     * Reads the file and splits it into chunks of the specified size, returning a list (for compatibility).
     * @param filePath Path to the file
     * @param linesPerChunk Number of lines per chunk
     * @return List of chunks
     * @throws IOException if file reading fails
     */
    public static List<Chunk> readText(Path filePath, int linesPerChunk) throws IOException {
        return readTextStream(filePath, linesPerChunk).collect(Collectors.toList());
    }

    private static Chunk createChunk(List<String> lines, int chunkIndex, int linesPerChunk) {
        int startLine = chunkIndex * linesPerChunk;
        int endLine = Math.min(startLine + linesPerChunk, lines.size());
        String chunkText = lines.subList(startLine, endLine).stream().collect(Collectors.joining(""));
        return new Chunk(chunkText, startLine);
    }
} 