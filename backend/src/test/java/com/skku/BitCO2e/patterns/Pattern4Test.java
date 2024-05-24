package com.skku.BitCO2e.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Pattern4Test {

    @Test
    public void testWrapperToPrimitiveConversion() {
        Pattern4 pattern = new Pattern4();
        String inputText = "public class Buggy {\n" +
                "    public static void main(String[] args) {\n" +
                "        Integer sum = 0;\n" +
                "        Long total = 0L;\n" +
                "        Double average = 0.0;\n" +
                "        for (int i = 0; i < 1_000_000_000; i++) {\n" +
                "            sum += i;\n" +
                "            total += i;\n" +
                "            average += i / 1_000_000_000.0;\n" +
                "        }\n" +
                "    }\n" +
                "}\n";

        String expectedOutput = "public class Fixed {\n" +
                "    public static void main(String[] args) {\n" +
                "        int sum = 0;\n" +
                "        long total = 0L;\n" +
                "        double average = 0.0;\n" +
                "        for (int i = 0; i < 1_000_000_000; i++) {\n" +
                "            sum += i;\n" +
                "            total += i;\n" +
                "            average += i / 1_000_000_000.0;\n" +
                "        }\n" +
                "    }\n" +
                "}";

        String result = pattern.main(inputText);
        System.out.println("Expected Output:\n" + expectedOutput); // 예상 출력
        System.out.println("Actual Output:\n" + result); // 실제 출력

        assertEquals(expectedOutput, result);
    }
}

