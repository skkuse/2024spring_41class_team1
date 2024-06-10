package com.skku.BitCO2e.patterns;

import java.util.regex.*;

public class Pattern12 {
    //Use BufferReader
    public String main(String inputText) {
        try {
            // 클래스명 수정
            inputText = inputText.replaceFirst("public class Buggy", "public class Fixed");

            // Find String concatenation within a loop
            Pattern concatPattern = Pattern.compile("String (\\w+) = \"\";(.*?)for \\((.*?)\\) \\{(.*?)\\1 \\+= (.*?);(.*?)\\}", Pattern.DOTALL);
            Matcher matcher = concatPattern.matcher(inputText);

            while (matcher.find()) {
                String varName = matcher.group(1);
                String beforeLoop = matcher.group(2);
                String loopDeclaration = matcher.group(3);
                String loopBodyBeforeConcat = matcher.group(4);
                String concatValue = matcher.group(5);
                String loopBodyAfterConcat = matcher.group(6);

                StringBuffer replacement = new StringBuffer();
                replacement.append("StringBuffer stringBuffer = new StringBuffer();");
                replacement.append(beforeLoop);
                replacement.append("for (").append(loopDeclaration).append(") {");
                replacement.append(loopBodyBeforeConcat);
                replacement.append("stringBuffer.append(").append(concatValue).append(");");
                replacement.append(loopBodyAfterConcat);
                replacement.append("}");
                replacement.append("String result = stringBuffer.toString();");

                inputText = inputText.replace(matcher.group(0), replacement.toString());
            }

            // Replace concatenation outside of loops
            inputText = inputText.replaceAll("String (\\w+) = \"\";", "StringBuffer $1 = new StringBuffer();");
            inputText = inputText.replaceAll("(\\w+) \\+= (.*?);", "$1.append($2);");
            inputText = inputText.replaceAll("System.out.println\\((.*?)\\);", "System.out.println($1.toString());");

            return inputText;

        } catch (Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }
}
