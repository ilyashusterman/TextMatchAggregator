package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
        BufferedReader reader = Files.newBufferedReader(filePath);
        Spliterator<Chunk> spliterator = new ChunkSpliterator(reader, linesPerChunk);
        // The stream will close the reader when done
        return StreamSupport.stream(spliterator, true).onClose(() -> {
            try { reader.close(); } catch (IOException ignored) {}
        });
    }

    /**
     * Custom Spliterator to lazily read and yield chunks from a BufferedReader.
     */
    private static class ChunkSpliterator extends Spliterators.AbstractSpliterator<Chunk> {
        private final BufferedReader reader;
        private final int linesPerChunk;
        private int currentLineOffset = 0;
        private boolean finished = false;

        protected ChunkSpliterator(BufferedReader reader, int linesPerChunk) {
            super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
            this.reader = reader;
            this.linesPerChunk = linesPerChunk;
        }

        @Override
        public boolean tryAdvance(Consumer<? super Chunk> action) {
            if (finished) return false;
            try {
                List<String> chunkLines = new ArrayList<>(linesPerChunk);
                String line;
                int linesRead = 0;
                while (linesRead < linesPerChunk && (line = reader.readLine()) != null) {
                    chunkLines.add(line);
                    linesRead++;
                }
                if (chunkLines.isEmpty()) {
                    finished = true;
                    return false;
                }
                String chunkText = String.join("", chunkLines);
                action.accept(new Chunk(chunkText, currentLineOffset));
                currentLineOffset += linesRead;
                if (linesRead < linesPerChunk) finished = true;
                return true;
            } catch (IOException e) {
                finished = true;
                throw new RuntimeException(e);
            }
        }
    }

    // The list-based methods remain for compatibility
    public static List<Chunk> readText(Path filePath) throws IOException {
        return readText(filePath, DEFAULT_LINES_PER_CHUNK);
    }

    public static List<Chunk> readText(Path filePath, int linesPerChunk) throws IOException {
        try (Stream<Chunk> stream = readTextStream(filePath, linesPerChunk)) {
            return stream.collect(java.util.stream.Collectors.toList());
        }
    }
} 