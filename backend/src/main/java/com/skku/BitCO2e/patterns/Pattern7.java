package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pattern7 {
    // Concat() is better than String.format()
    public String main(String inputText) {
        try {
            // Handle both single-line and multi-line inputs
            String[] lines = inputText.split("\\R");
            ArrayList<String> lineList = new ArrayList<>(Arrays.asList(lines));
            boolean isDetected = false;

            // Pattern to detect String.format usage
            Pattern pattern = Pattern.compile("String (\\w+) = String\\.format\\(\"([^%]*)%s([^%]*)%s([^%]*)\", (\\w+), (\\w+)\\);");

            // Detect and replace patterns
            for (int i = 0; i < lineList.size(); i++) {
                String line = lineList.get(i).trim();

                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    String variableName = matcher.group(1);
                    String firstSeparator = matcher.group(2);
                    String secondSeparator = matcher.group(3);
                    String thirdSeparator = matcher.group(4);
                    String firstVar = matcher.group(5);
                    String secondVar = matcher.group(6);

                    // Convert to concat if separators are short
                    if ((firstSeparator.length() + secondSeparator.length() + thirdSeparator.length()) < 5) {
                        String newLine = "String " + variableName + " = " + firstVar + ".concat(\""
                                + firstSeparator.trim() + secondSeparator.trim() + thirdSeparator.trim() + "\").concat(" + secondVar + ");";
                        lineList.set(i, line.replace(matcher.group(0), newLine)); // Replace only the matched pattern
                        isDetected = true;
                    }
                }
            }

            // Ensure the class name is updated regardless of pattern detection
            for (int i = 0; i < lineList.size(); i++) {
                String line = lineList.get(i).trim();
                if (line.contains("public class Buggy")) {
                    lineList.set(i, line.replace("public class Buggy", "public class Fixed"));
                    isDetected = true;
                    break;
                }
            }

            // Reconstruct the result with the updated lines
            return String.join("\n", lineList);

        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

}