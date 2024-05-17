package com.skku.BitCO2e.patterns;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 반복문 내에서 불필요하게 중복적으로 객체선언하는 에너지낭비패턴
public class Pattern3 {
    public String main(String inputText) {
        boolean isDetected = false;

        int classStartIndex = 0;

        int objectCreationIndex = 0;
        int loopCreationIndex = 0;

        try {
            // 코드 분할
            String[] codes = inputText.split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));

            // 검출
            int lineSize = lines.size();

            // find object creation
            for(int i=0; i<lineSize; i++) {
                String line = codes[i];
                if (line.contains("public class Buggy")) {
                    classStartIndex = i;
                    continue;
                }

                Pattern objectPattern = Pattern.compile("\\b[A-Z]\\w*\\s+[a-z]\\w*\\s*=\\s*new\\s+[A-Z]\\w*\\s*\\(\\s*\\)\\s*;");

                Matcher objectMatcher = objectPattern.matcher(line);
                if (objectMatcher.find()) {
                    System.out.println("Object creation detected: " + line);
                    objectCreationIndex = i;
                    break;
                }
            }

            // find for or while
            for(int i=0; i<lineSize; i++) {
                String line = codes[i];

                Pattern forPattern = Pattern.compile("\\b(?:for|while)\\s*\\(.*?\\)\\s*\\{");
                Matcher forMatcher = forPattern.matcher(line);
                if (forMatcher.find()) {
                    System.out.println("loop detected: " + line);
                    loopCreationIndex = i;
                    break;
                }
            }

//            System.out.println(lines.get(objectCreationIndex));
//            System.out.println(lines.get(loopCreationIndex));


            // 수정
            if(objectCreationIndex != 0) {
                lines.set(classStartIndex, "public class Fixed {\n");
                String fixedContent = lines.get(objectCreationIndex);

                lines.set(objectCreationIndex, "##MUSTDELETE##");
                lines.add(loopCreationIndex, fixedContent);

                lines.removeIf(item -> item.equals("##MUSTDELETE##"));

                for(int i=0; i<lines.size(); i++) {
                    System.out.println(i + " : " + lines.get(i));
                }

            }
            return String.join("\n", lines);

        } catch(Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

}