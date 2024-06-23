package com.skku.BitCO2e.patterns;

import java.util.*;
import java.util.regex.*;

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

            // Comparator 검출
            Pattern comparatorPattern = Pattern.compile("Collections\\.sort\\((.*?),\\s*new\\s*Comparator<.*?>\\(\\)\\s*\\{");
            Matcher comparatorMatcher = comparatorPattern.matcher(inputText);
            if (comparatorMatcher.find()) {
                comparatorStartIndex = getLineIndex(codes, comparatorMatcher.start());
                sortStartIndex = comparatorStartIndex;
                isDetected = true;
            }

            // 수정
            if (isDetected) {
                String sortLine = lines.get(sortStartIndex);
                String listName = sortLine.substring(sortLine.indexOf('(') + 1, sortLine.indexOf(',')).trim();

                // Find the end of the comparator
                int endOfComparatorIndex = findEndOfComparator(lines, comparatorStartIndex);

                // Extract comparator logic
                StringBuilder comparatorLogic = new StringBuilder();
                for (int i = comparatorStartIndex + 1; i < endOfComparatorIndex; i++) {
                    comparatorLogic.append(lines.get(i)).append("\n");
                }
                // Remove old comparator
                for (int i = comparatorStartIndex; i <= endOfComparatorIndex; i++) {
                    lines.set(i, "##MUSTDELETE##");
                }
                lines.removeIf(item -> item.equals("##MUSTDELETE##"));
                //test
                System.out.println("comparator"+comparatorLogic);

                // Stream.sorted 로 수정
                String lambdaFunction = createLambdaFunction(comparatorLogic.toString());

                lines.set(sortStartIndex, listName + " = " + listName + ".stream().sorted(" + lambdaFunction + ").collect(Collectors.toList());");


            }

            // import 추가
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
            count += lines[i].length() + 1;  // +1 for the newline
            if (count > charIndex) {
                return i;
            }
        }
        return -1;
    }

    //생성자 끝 검출
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

    //import 추가
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

    //클래스명 수정
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
