package org.example;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatcherUtil {
    public static Map<String, List<int[]>> matcher(FileReader.Chunk chunk, List<String> names) {
        Map<String, List<int[]>> results = new HashMap<>();
        // Build a regex to match any of the names as whole words, escaping special characters
        String patternString = "\\b(" + String.join("|", names.stream().map(MatcherUtil::escapeRegex).toList()) + ")\\b";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(chunk.getText());
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