package org.example;

import java.util.*;
import java.util.regex.Pattern;

public class Matcher {
    private static Pattern cachedPattern = null;
    private static List<String> cachedNames = null;

    public static void initializePattern(List<String> names) {
        if (cachedNames == null || !cachedNames.equals(names)) {
            String patternString = "\\b(" + String.join("|", names.stream().map(Matcher::escapeRegex).toList()) + ")\\b";
            cachedPattern = Pattern.compile(patternString);
            cachedNames = new ArrayList<>(names);
        }
    }

    public static Map<String, List<int[]>> matcher(FileReader.Chunk chunk, List<String> names) {
        if (cachedPattern == null || cachedNames == null || !cachedNames.equals(names)) {
            initializePattern(names);
        }
        Map<String, List<int[]>> results = new HashMap<>();
        java.util.regex.Matcher matcher = cachedPattern.matcher(chunk.getText());
        while (matcher.find()) {
            String name = matcher.group(1);
            int matchIndex = matcher.start();
            results.computeIfAbsent(name, k -> new ArrayList<>()).add(new int[]{chunk.getLineOffset(), matchIndex});
        }
        return results;
    }

    private static String escapeRegex(String s) {
        return Pattern.quote(s);
    }
} 