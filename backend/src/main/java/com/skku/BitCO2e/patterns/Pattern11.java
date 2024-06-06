package com.skku.BitCO2e.patterns;

import java.util.*;
import java.util.regex.*;
import java.util.stream.Collectors;

public class Pattern11 {
    //Comparator -> Sorted
    public String main(String inputText) {
        int comparatorStartIndex = -1;
        int sortStartIndex = -1;
        boolean isDetected = false;

        try {
            // 코드 분할
            String[] codes = inputText.split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));

            // Find Comparator
            Pattern comparatorPattern = Pattern.compile("Collections\\.sort\\((.*?),\\s*new\\s*Comparator<.*?>\\(\\)\\s*\\{");
            Matcher comparatorMatcher = comparatorPattern.matcher(inputText);
            if (comparatorMatcher.find()) {
                comparatorStartIndex = getLineIndex(codes, comparatorMatcher.start());
                sortStartIndex = comparatorStartIndex;
                isDetected = true;
            }

            // Modify
            if (isDetected) {
                String sortLine = lines.get(sortStartIndex);
                String listName = sortLine.substring(sortLine.indexOf('(') + 1, sortLine.indexOf(',')).trim();

                // Find the end of the comparator definition
                int endOfComparatorIndex = findEndOfComparator(lines, comparatorStartIndex);

                // Extract comparator logic
                StringBuilder comparatorLogic = new StringBuilder();
                for (int i = comparatorStartIndex + 1; i < endOfComparatorIndex; i++) {
                    comparatorLogic.append(lines.get(i)).append("\n");
                }
                // Remove old comparator definition
                for (int i = comparatorStartIndex; i <= endOfComparatorIndex; i++) {
                    lines.set(i, "##MUSTDELETE##");
                }
                lines.removeIf(item -> item.equals("##MUSTDELETE##"));

                System.out.println("comparator"+comparatorLogic);

                // Replace with Stream.sorted
                String lambdaFunction = createLambdaFunction(comparatorLogic.toString());

                lines.set(sortStartIndex, listName + " = " + listName + ".stream().sorted(" + lambdaFunction + ").collect(Collectors.toList());");


            }

            // Add import for Collectors if a change was made
            if (isDetected) {
                addCollectorsImportIfNeeded(lines);
                replaceClassNameIfNeeded(lines, "Buggy", "Fixed");
            }

            return String.join("\n", lines);

        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

    private int getLineIndex(String[] lines, int charIndex) {
        int count = 0;
        for (int i = 0; i < lines.length; i++) {
            count += lines[i].length() + 1;  // +1 for the newline character
            if (count > charIndex) {
                return i;
            }
        }
        return -1;
    }

    private int findEndOfComparator(ArrayList<String> lines, int startIndex) {
        int braceCount = 0;
        for (int i = startIndex; i < lines.size(); i++) {
            String line = lines.get(i);
            for (char c : line.toCharArray()) {
                if (c == '{') {
                    braceCount++;
                } else if (c == '}') {
                    braceCount--;
                }
            }
            if (braceCount == 0) {
                return i;
            }
        }
        return startIndex;
    }

    private String createLambdaFunction(String comparatorLogic) {
        return comparatorLogic
                .replaceAll("public int compare\\((.*?),\\s*(.*?)\\)\\s*\\{", "($1, $2) -> {")
                .replace("return", "")
                .replace(";", "")
                .replace("}", "")
                .trim();
    }

    private void addCollectorsImportIfNeeded(ArrayList<String> lines) {
        boolean hasCollectorsImport = lines.stream().anyMatch(line -> line.contains("import java.util.stream.Collectors;"));
        if (!hasCollectorsImport) {
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith("import ")) {
                    lines.add(i + 1, "import java.util.stream.Collectors;");
                    break;
                }
            }
        }
    }

    private void replaceClassNameIfNeeded(ArrayList<String> lines, String oldName, String newName) {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("public class " + oldName)) {
                lines.set(i, line.replace("public class " + oldName, "public class " + newName));
                break;
            }
        }
    }
}
