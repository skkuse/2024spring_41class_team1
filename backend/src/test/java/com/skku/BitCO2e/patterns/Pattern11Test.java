package com.skku.BitCO2e.patterns;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Pattern11Test {
    @Test
    public void test11(){
        String inputCode = "import java.util.ArrayList;\n" +
                "import java.util.Collections;\n" +
                "import java.util.Comparator;\n" +
                "import java.util.List;\n" +
                "\n" +
                "class Person {\n" +
                "    private String name;\n" +
                "    private int age;\n" +
                "\n" +
                "    public Person(String name, int age) {\n" +
                "        this.name = name;\n" +
                "        this.age = age;\n" +
                "    }\n" +
                "\n" +
                "    public String getName() {\n" +
                "        return name;\n" +
                "    }\n" +
                "\n" +
                "    public int getAge() {\n" +
                "        return age;\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "public class Buggy {\n" +
                "    public static void main(String[] args) {\n" +
                "        List<Person> people = new ArrayList<>();\n" +
                "        people.add(new Person(\"Alice\", 25));\n" +
                "        people.add(new Person(\"Bob\", 30));\n" +
                "        people.add(new Person(\"Charlie\", 20));\n" +
                "\n" +
                "        // 나이순으로 정렬\n" +
                "        Collections.sort(people, new Comparator<Person>() {\n" +
                "            public int compare(Person p1, Person p2) {\n" +
                "                return Integer.compare(p1.getAge(), p2.getAge());\n" +
                "            }\n" +
                "        });\n" +
                "\n" +
                "        // 정렬된 결과 출력\n" +
                "        for (Person person : people) {\n" +
                "            System.out.println(person.getName() + \" - \" + person.getAge());\n" +
                "        }\n" +
                "    }\n" +
                "}";

        String expectedOutputCode = "import java.util.ArrayList;\n" +
                "import java.util.stream.Collectors;\n"+
                "import java.util.Collections;\n" +
                "import java.util.Comparator;\n" +
                "import java.util.List;\n" +
                "\n" +
                "class Person {\n" +
                "    private String name;\n" +
                "    private int age;\n" +
                "\n" +
                "    public Person(String name, int age) {\n" +
                "        this.name = name;\n" +
                "        this.age = age;\n" +
                "    }\n" +
                "\n" +
                "    public String getName() {\n" +
                "        return name;\n" +
                "    }\n" +
                "\n" +
                "    public int getAge() {\n" +
                "        return age;\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "public class Fixed {\n" +
                "    public static void main(String[] args) {\n" +
                "        List<Person> people = new ArrayList<>();\n" +
                "        people.add(new Person(\"Alice\", 25));\n" +
                "        people.add(new Person(\"Bob\", 30));\n" +
                "        people.add(new Person(\"Charlie\", 20));\n" +
                "\n" +
                "        // 나이순으로 정렬\n" +
                "people = people.stream().sorted((Person p1, Person p2) -> {\n"+
                "                 Integer.compare(p1.getAge(), p2.getAge())).collect(Collectors.toList());\n" +
                "        // 정렬된 결과 출력\n" +
                "        for (Person person : people) {\n" +
                "            System.out.println(person.getName() + \" - \" + person.getAge());\n" +
                "        }\n" +
                "    }\n" +
                "}";

        Pattern11 pattern11 = new Pattern11();
        String result = pattern11.main(inputCode);

        assertEquals(expectedOutputCode, result, "The refactored code does not match the expected output.");
    }
}
