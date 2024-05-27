package com.skku.BitCO2e.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class Pattern7Test {

    @Test
    public void testSmallScaleConcatenation() {
        Pattern7 transformer = new Pattern7();
        String inputCode = "public class Buggy {\n"
                + "    String firstName = \"John\";\n"
                + "    String lastName = \"Doe\";\n"
                + "    String fullName = String.format(\"%s %s\", firstName, lastName);\n"
                + "}\n";

        String expectedOutput = "public class Fixed {\n"
                + "    String firstName = \"John\";\n"
                + "    String lastName = \"Doe\";\n"
                + "String fullName = firstName.concat(\"\").concat(lastName);\n"
                + "}";

        String actualOutput = transformer.main(inputCode);
        assertEquals(expectedOutput, actualOutput, "The output did not match the expected refactored code.");
    }

    @Test
    public void testLargeScaleConcatenation() {
        Pattern7 transformer = new Pattern7();
        String inputCode = "public class Buggy {\n"
                + "    String firstName = \"John\";\n"
                + "    String lastName = \"Doe\";\n"
                + "    String fullName = String.format(\"Hello, %s, how are you today? %s\", firstName, lastName);\n"
                + "}";

        // No change expected as the literals are too large
        String expectedOutput = inputCode;

        String actualOutput = transformer.main(inputCode);
        assertEquals(expectedOutput, actualOutput, "The output should not change for large-scale string formats.");
    }
}
