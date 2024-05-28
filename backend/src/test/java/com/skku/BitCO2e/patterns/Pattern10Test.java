package com.skku.BitCO2e.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class Pattern10Test {

    @Test
    public void testFileReadRefactoring() {
        Pattern10 transformer = new Pattern10();
        String inputCode =
                        "public class Buggy {\n" +
                        "    public static void main(String[] args) {\n" +
                        "        try {\n" +
                        "            FileInputStream fileInputStream = new FileInputStream(\"example.txt\");\n" +
                        "            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, \"UTF-8\"));\n" +
                        "            String line;\n" +
                        "            while ((line = bufferedReader.readLine()) != null) {\n" +
                        "                System.out.println(line); // 파일에서 한 줄씩 읽어 화면에 출력\n" +
                        "            }\n" +
                        "            bufferedReader.close();\n" +
                        "        } catch (IOException e) {\n" +
                        "            e.printStackTrace();\n" +
                        "        }\n" +
                        "    }\n" +
                        "}\n";

        String expectedOutput =
                        "public class Fixed {\n" +
                        "    public static void main(String[] args) {\n" +
                        "    List<String> lines = Files.readAllLines(Paths.get(\"파일경로\"), StandardCharsets.UTF_8);\n" +
                        "    for (String line : lines) {\n" +
                        "        System.out.println(line); // 파일에서 한 줄씩 읽어 화면에 출력\n" +
                        "    }\n" +
                        "}";

        String actualOutput = transformer.main(inputCode);
        assertEquals(expectedOutput, actualOutput, "The refactored code does not match the expected output.");
    }
}
