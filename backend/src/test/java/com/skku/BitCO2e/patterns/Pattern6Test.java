package com.skku.BitCO2e.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Pattern6Test {

    @Test
    public void testMain() {
        Pattern6 transformer = new Pattern6();
        String inputCode = "public class Buggy {\n"
                + "    Set<String> set = new HashSet<>();\n"
                + "    set.addAll(Arrays.asList(\"one\", \"two\", \"three\"));\n"
                + "}\n";

        String expectedOutput = "public class Fixed {\n"
                + "Set<String> set = new HashSet<>(Arrays.asList(\"one\", \"two\", \"three\"));\n"
                + "}";

        String actualOutput = transformer.main(inputCode);
        assertEquals(expectedOutput, actualOutput, "The output did not match the expected refactored code.");
    }
}
