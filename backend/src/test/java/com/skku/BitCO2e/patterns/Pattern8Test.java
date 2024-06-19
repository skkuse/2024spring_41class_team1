package com.skku.BitCO2e.patterns;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Pattern8Test {

    @Test
    public void testScannerToBufferedReaderTransformation() {
        Pattern8 transformer = new Pattern8();
        String inputCode =
                "import java.util.Scanner;\n" +
                        "public class Buggy {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        Scanner scanner = new Scanner(System.in);\n" +
                        "        for (int i = 0; i < 10; i++) {\n" +
                        "            String input = scanner.nextLine();\n" +
                        "            System.out.println(input);\n" +
                        "        }\n" +
                        "        scanner.close();\n" +
                        "    }\n" +
                        "}";

        String expectedOutput =
                "import java.io.BufferedReader;\n" +
                        "import java.io.InputStreamReader;\n" +
                        "import java.io.IOException;\n" +
                        "import java.util.Scanner;\n" +
                        "public class Fixed {\n" +
                        "    public static void main(String[] args) {\n" +
                        "BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));\n" +
                        "        for (int i = 0; i < 10; i++) {\n" +
                        "            String input = scanner.readLine();\n" +
                        "            System.out.println(input);\n" +
                        "        }\n" +
                        "        scanner.close();\n" +
                        "    }\n" +
                        "}";

        String actualOutput = transformer.main(inputCode);
        assertEquals(expectedOutput, actualOutput, "The refactored code does not match the expected output.");
    }
}
