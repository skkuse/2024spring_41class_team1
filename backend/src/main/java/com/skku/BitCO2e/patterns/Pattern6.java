package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Pattern6 {
    //Instantiate in Constructor when using Set
    public String main(String inputText) {
        boolean isDetected = false;
        StringBuilder result = new StringBuilder();

        try {
            // Handle both single-line and multi-line inputs
            String[] lines = inputText.split("\\R");
            ArrayList<String> lineList = new ArrayList<>(Arrays.asList(lines));

            // Pattern to detect Set instantiation and addAll with Arrays.asList
            Pattern singleLinePattern = Pattern.compile("\\bSet<(.+)>\\s+(\\w+)\\s*=\\s*new\\s+HashSet<>\\(\\);\\s*(\\w+)\\.addAll\\(Arrays\\.asList\\((.+)\\)\\);");

            // 검출 및 수정 in single line
            for (int i = 0; i < lineList.size(); i++) {
                String line = lineList.get(i).trim();

                Matcher matcher = singleLinePattern.matcher(line);

                if (matcher.find()) {
                    isDetected = true;
                    String genericType = matcher.group(1);
                    String variableName = matcher.group(2);
                    String elements = matcher.group(4);

                    // Create the new line combining both instantiation and initialization
                    String newLine = "Set<" + genericType + "> " + variableName + " = new HashSet<>(Arrays.asList(" + elements + "));";
                    lineList.set(i, line.replace(matcher.group(0), newLine)); // Replace the matched pattern
                }
            }

            // 검출 및 수정 in multi-line
            for (int i = 0; i < lineList.size() - 1; i++) {
                String line = lineList.get(i).trim();
                String nextLine = lineList.get(i + 1).trim();

                // Pattern to detect multi-line Set instantiation and initialization
                if (line.matches("\\s*Set<(.+)>\\s+(\\w+)\\s*=\\s*new\\s+HashSet<>\\(\\);")) {
                    String genericType = line.replaceFirst("^\\s*Set<(.+)>\\s+\\w+\\s*=.*$", "$1");
                    String variableName = line.replaceFirst("^\\s*Set<.+>\\s+(\\w+)\\s*=.*$", "$1");

                    if (nextLine.matches("\\s*" + variableName + "\\.addAll\\(Arrays\\.asList\\((.+)\\)\\);")) {
                        String elements = nextLine.replaceFirst("^\\s*\\w+\\.addAll\\(Arrays\\.asList\\((.+)\\)\\);", "$1");

                        // Create the new line combining both instantiation and initialization
                        String newLine = "Set<" + genericType + "> " + variableName + " = new HashSet<>(Arrays.asList(" + elements + "));";
                        lineList.set(i, line.replace(line, newLine)); // Replace the instantiation line
                        lineList.remove(i + 1); // Remove the addAll line
                        isDetected = true;
                        i--; // Adjust loop counter to reflect the removed line
                    }
                }
            }

            // 클래스 명 수정
            for (int i = 0; i < lineList.size(); i++) {
                String line = lineList.get(i).trim();
                if (line.contains("public class Buggy")) {
                    lineList.set(i, line.replace("public class Buggy", "public class Fixed"));
                    isDetected = true;
                    break;
                }
            }

            // Reconstruct the result with the updated lines
            for (String line : lineList) {
                result.append(line).append("\n");
            }

            return result.toString().trim();

        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

}