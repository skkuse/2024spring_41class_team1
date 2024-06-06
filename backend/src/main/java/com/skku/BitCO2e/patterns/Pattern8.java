package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;

public class Pattern8 {
    //Scanner vs BufferedReader
    public String main(String inputText) {
        try {
            // 코드 분할
            String[] codes = inputText.split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));
            boolean isDetected = false;
            String scannerVariableName = null; // Store the variable name used for Scanner

            // 검출 및 수정
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();

                // Detect Scanner
                if (line.contains("new Scanner(System.in)")) {
                    scannerVariableName = line.replaceAll(".*?(\\w+)\\s*=\\s*new Scanner\\(System.in\\);.*", "$1");
                    lines.set(i, "BufferedReader " + scannerVariableName + " = new BufferedReader(new InputStreamReader(System.in));");
                    isDetected = true;
                }
                // 수정
                else if (scannerVariableName != null && line.contains(scannerVariableName + ".nextLine()")) {
                    lines.set(i, line.replace(scannerVariableName + ".nextLine();", scannerVariableName + ".readLine();"));
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

