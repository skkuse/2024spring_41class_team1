package com.skku.BitCO2e.patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Pattern6 {
    //Instantiate in constructor
    public String main(String inputText) {
        boolean isDetected = false;
        int classStartIndex = 0;
        try {
            // Split the input text into lines.
            String[] codes = inputText.split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));


            // Loop through the lines to detect patterns.
            for (int i = 0; i < lines.size() - 1; i++) {
                String line = lines.get(i).trim();
                String nextLine = i + 1 < lines.size() ? lines.get(i + 1).trim() : "";

                if (line.matches("\\s*Set<(.+)>\\s+(\\w+)\\s*=\\s*new\\s+HashSet<>\\(\\);")) {
                    String genericType = line.replaceFirst("^\\s*Set<(.+)>\\s+\\w+\\s*=.*$", "$1");
                    String variableName = line.replaceFirst("^\\s*Set<.+>\\s+(\\w+)\\s*=.*$", "$1");

                    if (nextLine.matches("\\s*" + Pattern.quote(variableName) + "\\.addAll\\(Arrays\\.asList\\((.+)\\)\\);")) {
                        String elements = nextLine.replaceFirst("^\\s*\\w+\\.addAll\\(Arrays\\.asList\\((.+)\\)\\);", "$1");

                        // Create the new line combining both instantiation and initialization.
                        String newLine = "Set<" + genericType + "> " + variableName + " = new HashSet<>(Arrays.asList(" + elements + "));";
                        lines.set(i, newLine); // Replace the instantiation line.
                        lines.remove(i + 1); // Remove the addAll line.
                        isDetected = true;
                        i--; // Adjust loop counter to reflect the removed line.
                    }
                }
            }

            // Check if class name should be updated.
            if (isDetected) {
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    if (line.contains("public class Buggy")) {
                        classStartIndex = i;
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

