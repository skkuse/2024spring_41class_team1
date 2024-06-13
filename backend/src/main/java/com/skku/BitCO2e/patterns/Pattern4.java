package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pattern4 {
    // Primitives vs Wrapper Objects
    public String main(String inputText) {
        boolean isDetected = false;
        StringBuilder result = new StringBuilder();

        try {
            // 코드 분할
            String[] lines = inputText.split("\n");
            ArrayList<String> lineList = new ArrayList<>(Arrays.asList(lines));

            // Wrapper to primitive mapping
            String[][] wrapperToPrimitive = {
                    {"Integer", "int"},
                    {"Long", "long"},
                    {"Double", "double"},
                    {"Float", "float"},
                    {"Character", "char"},
                    {"Byte", "byte"},
                    {"Short", "short"},
                    {"Boolean", "boolean"}
            };

            // 검출 및 수정
            for (int i = 0; i < lineList.size(); i++) {
                String line = lineList.get(i);

                for (String[] pair : wrapperToPrimitive) {
                    String wrapper = pair[0];
                    String primitive = pair[1];

                    // 패턴 컴파일
                    Pattern pattern = Pattern.compile("\\b" + wrapper + "\\b");
                    Matcher matcher = pattern.matcher(line);

                    // 매칭되는 라인이 있으면 교체
                    if (matcher.find()) {
                        isDetected = true;
                        line = matcher.replaceAll(primitive);
                    }
                }

                result.append(line).append("\n");
            }

            // 클래스명 수정
            if (isDetected) {
                String modifiedCode = result.toString();
                if (modifiedCode.contains("public class Buggy")) {
                    modifiedCode = modifiedCode.replace("public class Buggy", "public class Fixed");
                }
                return modifiedCode;
            }

            return result.toString();

        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}