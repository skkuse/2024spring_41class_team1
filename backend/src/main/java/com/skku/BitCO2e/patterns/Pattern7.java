package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pattern7 {
    //Concat() is better than String.format()
    public String main(String inputText) {
        try {
            // 코드 분할
            String[] codes = inputText.split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));
            boolean isDetected = false;

            // 검출 및 수정
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();

                // Improved regex to match simple and short String.format usage
                Pattern pattern = Pattern.compile("String (\\w+) = String\\.format\\(\"([^%]*)%s([^%]*)%s([^%]*)\", (\\w+), (\\w+)\\);");
                Matcher matcher = pattern.matcher(line);

                if (matcher.find()) {
                    String variableName = matcher.group(1);
                    String firstSeparator = matcher.group(2);
                    String secondSeparator = matcher.group(3);
                    String thirdSeparator = matcher.group(4);
                    String firstVar = matcher.group(5);
                    String secondVar = matcher.group(6);

                    // 짧은 문자열일 때만 concat으로 바꿈
                    if ((firstSeparator.length() + secondSeparator.length() + thirdSeparator.length()) < 5) { // Assuming 'small-scale' means less than 5 characters total
                        // Replace with concat
                        String newLine = "String " + variableName + " = " + firstVar + ".concat(\""
                                + firstSeparator.trim() + secondSeparator.trim() + "\").concat(" + secondVar + ");";
                        lines.set(i, newLine);
                        isDetected = true;
                    }
                }
            }

            // 클래스명 수정
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
}
