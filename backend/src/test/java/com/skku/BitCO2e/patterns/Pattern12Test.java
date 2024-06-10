package com.skku.BitCO2e.patterns;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Pattern12Test {
    @Test
    public void test() {
        String inputText = "public class Buggy {\n" +
                "    public static void main(String[] args) {\n" +
                "        // 사용할 문자열들\n" +
                "        String[] words = {\"Hello\", \" \", \"World\", \"!\"};\n" +
                "\n" +
                "        String concatenatedString = \"\";\n" +
                "        for (String word : words) {\n" +
                "            concatenatedString += word;\n" +
                "        }\n" +
                "    }\n" +
                "}";

        String expectedOutput = "public class Fixed {\n" +
                "    public static void main(String[] args) {\n" +
                "        // 사용할 문자열들\n" +
                "        String[] words = {\"Hello\", \" \", \"World\", \"!\"};\n" +
                "\n" +
                "        StringBuffer stringBuffer = new StringBuffer();\n" +
                "        for (String word : words) {\n" +
                "            stringBuffer.append(word);\n" +
                "        }String result = stringBuffer.toString();\n" +
                "    }\n" +
                "}";

        Pattern12 pattern12 = new Pattern12();
        String actualOutput = pattern12.main(inputText);

        System.out.println("Expected Output:");
        System.out.println(expectedOutput);
        System.out.println("\nActual Output:");
        System.out.println(actualOutput);

        assertEquals(expectedOutput, actualOutput, "The refactored code does not match the expected output.");
    }
}
