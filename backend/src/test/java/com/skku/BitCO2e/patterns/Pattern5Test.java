package com.skku.BitCO2e.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Pattern5Test {

    @Test
    public void testMain() {
        Pattern5 transformer = new Pattern5();
        String inputCode = "public class Buggy {\n"
                + "    int sum(int a, int b) {\n"
                + "        int temp = a + b;\n"
                + "        return temp;\n"
                + "    }\n"
                + "}\n";

        String expectedOutput = "public class Fixed {\n"
                + "    int sum(int a, int b) {\n"
                + "return (a + b);\n"
                + "    }\n"
                + "}";

        String actualOutput = transformer.main(inputCode);
        assertEquals(expectedOutput, actualOutput, "The output did not match the expected refactored code.");
    }
}
