package com.skku.BitCO2e.patterns;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 2중 또는 3중 중첩반복문을 통해 불필요한 연산수행하는 에너지낭비패턴
public class Pattern2 {
    public String main(String inputText) {
        boolean isDetected = false;
        int classStartIndex = 0;

        int firstStartIfIndex = 0;
        int firstEndIfIndex = 0;
        int thirdStartIfIndex = 0;
        int thirdEndIfIndex = 0;

        String firstCondition = "";
        String secondCondition = "";
        String thirdCondition = "";


        try {
            // 코드 분할
            String[] codes = inputText.split("\n");
            ArrayList<String> lines = new ArrayList<>(Arrays.asList(codes));

            // 검출
            int lineSize = lines.size();

            // 중첩된 if 문 여부 플래그
            boolean nestedIfFound = false;

            // 조건식 추출 정규식
            Pattern pattern = Pattern.compile("\\((.*?)\\)");

            for(int i=0; i<lineSize; i++) {
                String line = codes[i];
                if (line.contains("public class Buggy")) {
                    classStartIndex = i;
                    continue;
                }


                if (line.contains("if(")) {
                    Matcher matcher1 = pattern.matcher(line);
                    firstStartIfIndex = i;

                    if(matcher1.find()) {
                        firstCondition = matcher1.group(1);
                    }


                    // 현재 줄에 중첩된 if 문이 있는지 추가 검사 (3개 중첩인지 검사)
                    for (int j = i+1; j < lineSize; j++) {
                        String line2 = lines.get(j);

                        if(line2.contains("if(")) {

                            Matcher matcher2 = pattern.matcher(line2);

                            if(matcher2.find()) {
                                secondCondition = matcher2.group(1);
                            }


                            for(int k = j+1; k < lineSize; k++) {
                                String line3 = lines.get(k);

                                if(line3.contains("if(")) {
                                    nestedIfFound = true;
                                    thirdStartIfIndex = k;

                                    Matcher matcher3 = pattern.matcher(line3);

                                    if(matcher3.find()) {
                                        thirdCondition = matcher3.group(1);
                                    }


                                    for(int l = k+1; l < lineSize; l++) {
                                        String line4 = lines.get(l);
                                        if(line4.contains("}")) {
                                            thirdEndIfIndex = l;
                                            break;
                                        }
                                    }

                                    int count = 0;

                                    for(int l = k+1; l < lineSize; l++) {
                                        String line4 = lines.get(l);

                                        if(line4.contains("}")) {
                                            count++;
                                            if(count==3) {
                                                firstEndIfIndex = l;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if(thirdEndIfIndex != 0) break;
                            }
                        }
                        if(thirdEndIfIndex != 0) break;
                    }
                    // 중첩된 if 문이 발견되면 반복문 종료
                    if (nestedIfFound) {
                        break;
                    }
                }
            }


            // 수정
            if (nestedIfFound) {
                lines.set(classStartIndex, "public class Fixed {\n");
                String conditionBody = "";
                for(int i=thirdStartIfIndex+1; i<thirdEndIfIndex; i++) {
                    conditionBody = conditionBody + (lines.get(i) + "\n");
                }
//
//
//
//
                System.out.println("주어진 Java 파일에 중첩된 if 문이 있습니다." + firstStartIfIndex + ", " + firstEndIfIndex);

//                int until = firstEndIfIndex - firstStartIfIndex;
                for(int i=firstStartIfIndex; i<=firstEndIfIndex; i++) {
                    lines.set(i, "##MUSTDELETE##");
                }
////
////
////
                lines.set(firstStartIfIndex-1, "\t\tif((" + firstCondition + " && " + secondCondition + ") && " + thirdCondition + ") {\n");
                lines.add(firstStartIfIndex, "\t\t\t" + conditionBody + "\n");
                lines.add(firstStartIfIndex+1, "\t\t}\n");

                lines.removeIf(item -> item.equals("##MUSTDELETE##"));
                int realSize = lines.size();
                for(int i=0; i<realSize; i++) {
                    System.out.println(i + " : " + lines.get(i));
                }
            } else {
                System.out.println("주어진 Java 파일에 중첩된 if 문이 없습니다.");
            }
            return String.join("\n", lines);

        } catch(Exception e) {
            System.out.println(e);
            return e.toString();
        }
    }

}