package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Pattern8 {
    // Scanner vs BufferedReader
    public String main(String inputText) {
        try {
            // Handle both single-line and multi-line inputs
            String[] lines = inputText.split("\\R");
            ArrayList<String> lineList = new ArrayList<>(Arrays.asList(lines));
            boolean isDetected = false;
            String scannerVariableName = null; // Store the variable name used for Scanner

            // 검출 및 수정
            for (int i = 0; i < lineList.size(); i++) {
                String line = lineList.get(i).trim();

                // Scanner instantiation 검출
                Pattern scannerPattern = Pattern.compile("Scanner\\s+(\\w+)\\s*=\\s*new\\s+Scanner\\(System.in\\);");
                Matcher scannerMatcher = scannerPattern.matcher(line);

                if (scannerMatcher.find()) {
                    scannerVariableName = scannerMatcher.group(1);
                    lineList.set(i, line.replace(scannerMatcher.group(0), "BufferedReader " + scannerVariableName + " = new BufferedReader(new InputStreamReader(System.in));"));
                    isDetected = true;

                }
            }
            //nextLine 을 readLine 으로 수정
            if (scannerVariableName != null) {
                Pattern nextLinePattern = Pattern.compile(scannerVariableName + "\\.nextLine\\(\\)");
                for (int i = 0; i < lineList.size(); i++) {
                    Matcher nextLineMatcher = nextLinePattern.matcher(lineList.get(i));
                    if (nextLineMatcher.find()) {
                        lineList.set(i, nextLineMatcher.replaceAll(scannerVariableName + ".readLine()"));
                    }
                }
            }

            // 클래스 명 수정
            for (int i = 0; i < lineList.size(); i++) {
                String line = lineList.get(i).trim();
                if (line.contains("public class Buggy")) {
                    lineList.set(i, line.replace("public class Buggy", "public class Fixed"));
                    break;
                }
            }

            // Ensure try-catch block is added for BufferedReader usage
            if (isDetected) {
                for (int i = 0; i < lineList.size(); i++) {
                    String line = lineList.get(i).trim();
                    if (line.contains("public static void main")) {
                        int braceIndex = i + 1;
                        while (braceIndex < lineList.size() && !lineList.get(braceIndex).trim().equals("{")) {
                            braceIndex++;
                        }
                        if (braceIndex < lineList.size()) {
                            lineList.add(braceIndex + 1, "try {");
                            for (int j = braceIndex + 2; j < lineList.size(); j++) {
                                String codeLine = lineList.get(j).trim();
                                if (codeLine.equals("}")) {
                                    lineList.add(j, "} catch (IOException e) { e.printStackTrace(); }");
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }

            // import 추가
            if (isDetected && !inputText.contains("import java.io.BufferedReader;")) {
                lineList.add(0, "import java.io.BufferedReader;");
                lineList.add(1, "import java.io.InputStreamReader;");
                lineList.add(2, "import java.io.IOException;");
            }

            return String.join("\n", lineList);

        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}

