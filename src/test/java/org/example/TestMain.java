package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class TestMain {
    @Test
    void testMainWithBigTxt() {
        String[] args = {"./big.txt"};
        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
        try {
            Main.main(args);
        } catch (Exception e) {
            fail("Main.main threw an exception: " + e.getMessage());
        } finally {
            System.setOut(originalOut);
            System.setErr(originalErr);
        }
        String output = outContent.toString();
        assertTrue(output.contains("[main] main() called with inputFile: ./big.txt"), "Should print main start message");
        assertTrue(output.contains("RESULTS"), "Should print results section");
        assertTrue(output.contains("[printer] Timothy --> [[lineOffset=13000, charOffset=19775], [lineOffset=13000, charOffset=42023]]"),
            "Output should contain the expected printer line for Timothy");
    }
} 