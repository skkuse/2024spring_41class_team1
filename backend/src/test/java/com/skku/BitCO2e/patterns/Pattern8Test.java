package com.skku.BitCO2e.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Pattern8Test {

    @Test
    public void testScannerToBufferedReaderTransformation() {
        Pattern8 transformer = new Pattern8();
        String inputCode =
                "public class Buggy {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        Scanner scanner = new Scanner(System.in);\n" +
                        "        for (int i = 0; i < 10; i++) {\n" +
                        "            String input = scanner.nextLine();\n" +
                        "            System.out.println(input);\n" +
                        "        }\n" +
                        "        scanner.close();\n" +
                        "    }\n" +
                        "}\n";

        String expectedOutput =
                "public class Fixed {\n" +
                        "    public static void main(String[] args) {\n" +
                        "BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));\n" +
                        "        for (int i = 0; i < 10; i++) {\n" +
                        "String input = scanner.readLine();\n" +
                        "            System.out.println(input);\n" +
                        "        }\n" +
                        "        scanner.close();\n" +
                        "    }\n" +
                        "}";

        String actualOutput = transformer.main(inputCode);
        assertEquals(expectedOutput, actualOutput, "The refactored code does not match the expected output.");
    }
}
