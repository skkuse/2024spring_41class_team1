package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pattern9 {
    public String main(String inputText) {
        try {

            String[] codes = inputText.split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));
            boolean isDetected = false;

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();

                // detect large array
                Pattern arrayPattern = Pattern.compile("int\\[\\]\\s*(\\w+)\\s*=\\s*\\{([^}]+)\\};");
                Matcher arrayMatcher = arrayPattern.matcher(line);

                if (arrayMatcher.find()) {
                    String arrayName = arrayMatcher.group(1);
                    String[] elements = arrayMatcher.group(2).split("\\s*,\\s*");

                    // Check if the array is "large"
                    if (elements.length > 5) {
                        int sumLineIndex = findSumLineIndex(lines, arrayName, i + 1);
                        if (sumLineIndex != -1) {
                            lines.set(sumLineIndex, "int sum = IntStream.of(" + arrayName + ").sum();");
                            removeLoopLines(lines, sumLineIndex + 1);
                            isDetected = true;
                        }
                    }
                }
            }

            // Class명 수정
            if (isDetected) {
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    if (line.contains("public class Buggy")) {
                        lines.set(i, line.replace("public class Buggy", "public class Fixed"));
                    }
                }
            }

            return String.join("\n", lines);

        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

    // Method to find the index of sum calculation using a for-loop
    private int findSumLineIndex(ArrayList<String> lines, String arrayName, int startIndex) {
        for (int i = startIndex; i < lines.size(); i++) {
            if (lines.get(i).contains("int sum = 0;")) {
                return i;
            }
        }
        return -1;
    }

    private void removeLoopLines(ArrayList<String> lines, int startIndex) {
        int endIndex = startIndex;
        while (!lines.get(endIndex).trim().equals("}")) { // Assuming the loop ends with a }
            endIndex++;
        }
        for (int i = startIndex; i <= endIndex; i++) {
            lines.remove(startIndex);
        }
    }
}


