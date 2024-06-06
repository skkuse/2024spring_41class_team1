package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pattern4 {
    //Primitives vs Wrapper Objects
    public String main(String inputText) {
        boolean isDetected = false;
        int classStartIndex = 0;

        try {
            // 코드 분할
            String[] codes = inputText.split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));

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
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);

                for (String[] pair : wrapperToPrimitive) {
                    String wrapper = pair[0];
                    String primitive = pair[1];

                    // 패턴 컴파일
                    Pattern pattern = Pattern.compile("\\b" + wrapper + "\\b");
                    Matcher matcher = pattern.matcher(line);

                    // 매칭되는 라인이 있으면 교체
                    if (matcher.find()) {
                        isDetected = true;
                        lines.set(i, matcher.replaceAll(primitive));
                    }
                }
            }

            // 클래스명 수정
            if (isDetected) {
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    if (line.contains("public class Buggy")) {
                        classStartIndex = i;
                        lines.set(i, line.replace("public class Buggy", "public class Fixed"));
                        break;
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