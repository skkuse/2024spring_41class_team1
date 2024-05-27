package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;

public class Pattern5 {
    public String main(String inputText) {
        boolean isDetected = false;
        int classStartIndex = 0;
        try {
            // 코드 분할
            String[] codes = inputText.split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));

            // 검출 및 수정
            for (int i = 0; i < lines.size() - 1; i++) {
                String line = lines.get(i).trim();

                // Check if temp and the next line returns it
                if (line.matches("\\s*int temp = .+;")) {
                    isDetected = true;
                    String nextLine = lines.get(i + 1).trim();

                    if (nextLine.equals("return temp;")) {
                        String expression = line.substring(line.indexOf('=') + 1, line.length() - 1).trim();
                        lines.set(i, "return (" + expression + ");"); // Set the current line to return the expression (괄호로 묶어서)
                        lines.remove(i + 1); // return of temp 지움
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

