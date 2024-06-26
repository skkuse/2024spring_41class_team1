package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Pattern5 {
    // Skipping Temporary Variable
    public String main(String inputText) {
        boolean isDetected = false;
        StringBuilder result = new StringBuilder();

        try {
            // Handle both single-line and multi-line inputs
            String[] lines = inputText.split("\\R");
            ArrayList<String> lineList = new ArrayList<>(Arrays.asList(lines));

            // Pattern to detect 'int temp = ...;' and 'return temp;'
            Pattern singleLinePattern = Pattern.compile("\\bint temp = (.+?); return temp;");

            // 검출 및 수정
            for (int i = 0; i < lineList.size(); i++) {
                String line = lineList.get(i).trim();

                Matcher singleLineMatcher = singleLinePattern.matcher(line);

                if (singleLineMatcher.find()) { //single line 에서는 같은 줄
                    isDetected = true;
                    String expression = singleLineMatcher.group(1).trim();
                    lineList.set(i, line.replace(singleLineMatcher.group(0), "return (" + expression + ");")); // Replace with return statement
                } else if (i < lineList.size() - 1) { //multiline - 다음 줄에 return temp 검출
                    // Check if temp and the next line returns it
                    String nextLine = lineList.get(i + 1).trim();
                    if (line.matches("\\s*int temp = .+;") && nextLine.equals("return temp;")) {
                        isDetected = true;
                        String expression = line.substring(line.indexOf('=') + 1, line.length() - 1).trim();
                        lineList.set(i, "return (" + expression + ");"); // Set to return the expression
                        lineList.remove(i + 1); // Remove the next line (return temp;)
                    }
                }
            }

            // 클래스 명 수정
            if (isDetected) {
                for (int i = 0; i < lineList.size(); i++) {
                    String line = lineList.get(i);
                    if (line.contains("public class Buggy")) {
                        lineList.set(i, line.replace("public class Buggy", "public class Fixed"));
                        break;
                    }
                }
            }

            return String.join("\n", lineList);

        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}
