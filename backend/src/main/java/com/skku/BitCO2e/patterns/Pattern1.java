package com.skku.BitCO2e.patterns;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// arraylist 객체의 사이즈를 반복문의 조건에서 불필요하게 계속 구하는 에너지낭비로직
public class Pattern1 {
    public String main(String inputText) {
        boolean isDetected = false;
        int classStartIndex = 0;

        try {
            // 코드 분할
            String[] codes = inputText.split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));

            // 검출
            int buggyLine = -1;
            String arrayListVariableName = "";
            int lineSize = lines.size();

            for(int i=0; i<lineSize; i++) {
                String line = codes[i];
                if(line.contains("public class Buggy")) {
                    classStartIndex = i;
                    continue;
                }

                Pattern pattern = Pattern.compile("\\bArrayList\\s*<[^>]*>\\s+([a-zA-Z0-9_]+)\\s*=");

                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    arrayListVariableName = matcher.group(1);

                    for(int j=i; j<codes.length; j++) {
                        String line2 = codes[j];
                        Pattern sizeMethodPattern = Pattern.compile("\\b" + arrayListVariableName + "\\.size\\(\\)");

                        Matcher sizeMethodMatcher = sizeMethodPattern.matcher(line2);
                        if (sizeMethodMatcher.find()) {
//                            System.out.println("ArrayList 객체 변수명: " + arrayListVariableName);
//                            System.out.println("해당 변수를 사용하여 size() 메서드를 호출함");
                            buggyLine = j;
                            isDetected = true;
                            break;
                        }
                    }
                }
            }

            // 수정
            if(isDetected) {
                int count = countLeadingSpaces(lines.get(buggyLine));
                lines.set(classStartIndex, "public class Fixed {");

                lines.set(buggyLine, lines.get(buggyLine).replace(arrayListVariableName + ".size()", arrayListVariableName + "Size"));

                StringBuilder builder = new StringBuilder();
                for(int i=0; i<count; i++) {
                    builder.append(' ');
                }
                builder.append("int " + arrayListVariableName + "Size = " + arrayListVariableName + ".size();\n");
                lines.add(buggyLine, builder.toString());
            }
            return String.join("\n", lines);

        } catch(Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

    public static int countLeadingSpaces(String text) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}