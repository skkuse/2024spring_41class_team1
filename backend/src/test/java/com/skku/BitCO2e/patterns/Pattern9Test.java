package com.skku.BitCO2e.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class Pattern9Test {

    @Test
    public void testArraySumRefactoringForLargeArray() {
        Pattern9 transformer = new Pattern9();
        String inputCode =
                "public class Buggy {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int[] arr = {1, 2, 3, 4, 5, 6, 7};\n" +
                        "        int sum = 0;\n" +
                        "        for (int i = 0; i < arr.length; i++) {\n" +
                        "            sum += arr[i];\n" +
                        "        }\n" +
                        "        System.out.println(\"배열의 합: \" + sum);\n" +
                        "    }\n" +
                        "}\n";

        String expectedOutput =
                "public class Fixed {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int[] arr = {1, 2, 3, 4, 5, 6, 7};\n" +
                        "int sum = IntStream.of(arr).sum();\n" +
                        "        System.out.println(\"배열의 합: \" + sum);\n" +
                        "    }\n" +
                        "}";

        String actualOutput = transformer.main(inputCode);
        assertEquals(expectedOutput, actualOutput, "The refactored code does not match the expected output for a large array.");
    }

    @Test
    public void testArraySumRefactoringForSmallArray() { //조그만 배열은 굳이 안바꿈
        Pattern9 transformer = new Pattern9();
        String inputCode =
                "public class Buggy {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        int[] arr = {1, 2, 3};\n" +
                        "        int sum = 0;\n" +
                        "        for (int i = 0; i < arr.length; i++) {\n" +
                        "            sum += arr[i];\n" +
                        "        }\n" +
                        "        System.out.println(\"배열의 합: \" + sum);\n" +
                        "    }\n" +
                        "}";

        String expectedOutput = inputCode; // No change as the array is not large

        String actualOutput = transformer.main(inputCode);
        assertEquals(expectedOutput, actualOutput, "The code should not be refactored for a small array.");
    }
}
