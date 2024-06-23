import React, { useState, useEffect } from 'react';
import { styled, Box, Table, TableBody, TableCell, TableRow, Button } from "@mui/material";
import PatternDialog from '../components/PatternDialog';

const Section = styled(Box)({
    minHeight: 30,
    padding: 20,
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
});

const StyledTable = styled(Table)({
    border: "2px solid #333", // 진한 테두리
    borderColor: "#333", // 테두리 색상
    width: "500px",
});

const CenteredTableCell = styled(TableCell)({
    fontWeight: 'bold', // 진한 글씨
    textAlign: 'center', // 텍스트 가운데 정렬
    verticalAlign: 'middle',
    margin: 'auto',
});

const ImageCell = styled(TableCell)({
    textAlign: 'center',
    display: 'flex',          // Flex 컨테이너를 사용
    justifyContent: 'center', // 수평 중앙 정렬
    alignItems: 'center',     // 수직 중앙 정렬
});

const ButtonContainer = styled(Box)({
    display: 'flex',
    justifyContent: 'center',  // 수평 중앙 정렬
    alignItems: 'center',      // 수직 중앙 정렬
    width: '100%',             // 부모 요소의 전체 너비 사용
    paddingTop: "8px",
});

const MainBottomPage = ({ code }) => {
    const [refactoredCode, setRefactoredCode] = useState("");  // 변환된 코드를 저장할 상태
    const [isLoading, setIsLoading] = useState(false);         // 로딩 상태
    const [patterns, setPatterns] = useState([]);
    const [open, setOpen] = useState(false);  // 대화 상자의 열림/닫힘 상태
    const [currentPattern, setCurrentPattern] = useState(null);  // 현재 선택된 패턴
    const [compareData, setCompareData] = useState(null);

    const patternDescriptions = {   //각 패턴에 대한 설명, before/after 코드
        Pattern1: {
            description: "Strength reduction and binary operators",
            before: "if (number % 2 == 0)",
            after: "if ((number & 1) == 0)"
        },
        Pattern2: {
            description: "Avoid multiple if-else statements",
            before: `public boolean foo() {
    if (condition1) {
        if (condition2) {
            if (condition3 || condition4) {
                return true;
            } else {
                return false;
            }
        }
    }
    return false;
}`,
            after: `boolean result = (condition1 && condition2) && (condition3 || condition4)
public boolean conditionsMet() {
    return (condition1 && condition2) && (condition3 || condition4);
}`
        },
        Pattern3: {
            description: "반복문 내에서 불필요하게 중복적으로 객체선언하는 에너지낭비패턴",
            before: `public class InefficientExample {
    public static void main(String[] args) {
        String result = "";
        for (int i = 0; i < 100; i++) {
            StringBuilder builder = new StringBuilder(); // 비효율적인 위치
            builder.append("Line ").append(i);
            result += builder.toString();
        }
        System.out.println(result);
    }
}
`,
            after: `public class EfficientExample {
    public static void main(String[] args) {
        StringBuilder builder = new StringBuilder(); // 효율적인 위치
        for (int i = 0; i < 100; i++) {
            builder.append("Line ").append(i).append("\n"); // 매 반복마다 내용 추가
        }
        String result = builder.toString();
        System.out.println(result);
    }
}
`
        },
        Pattern4: {
            description: "Primitives vs Wrapper Objects",
            before: `with Wrappers:
Integer sum = 0;
for (int i = 0; i < 1_000_000_000; i++) {
    sum += i;
}`,
            after: `with Primitives only:
int sum = 0;
for (int i = 0; i < 1_000_000_000; i++)  {
    sum += i;
}`
        },
        Pattern5: {
            description: "Skipping Temporary Variable",
            before: `int sum(int a, int b){
		int temp = a + b ;
		
		return temp;
}`,
            after: `int sum(int a, int b){
		return (a+b);
}`
        },
        Pattern6: {
            description: "Instantiate in constructor",
            before: `Set<String>set = new HashSet<>();
set.addAll(Arrays.asList("one","two","three"));
`,
            after: `Set<String>set = new HashSet<>(Arrays.asList("one","two","three"));`
        },
        Pattern7: {
            description: "Concat() is better than String.format()",
            before: `String firstName = "John";
String lastName = "Doe";
String fullName = String.format("%s %s", firstName, lastName);`,
            after: `String firstName = "John";
String lastName = "Doe";
String fullName = firstName.concat(" ").concat(lastName);`
        },
        Pattern8: {
            description: "Scanner vs BufferedReader",
            before: `Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < iterations; i++) {
            scanner.nextLine();
        }
        scanner.close();`,
            after: `BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        for (int i = 0; i < iterations; i++) {
            bufferedReader.readLine();
        }
        bufferedReader.close();`
        },
        Pattern9: {
            description: "IntStream.of(배열).sum()",
            before: `public class ArraySumBefore {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};

        // 배열의 합을 계산
        int sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }

        System.out.println("배열의 합: " + sum);
    }
}`,
            after: `import java.util.stream.IntStream;

public class ArraySumAfter {
    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5};

        // 배열의 합을 계산
        int sum = IntStream.of(arr).sum();

        System.out.println("배열의 합: " + sum);
    }
}`
        },
        Pattern10: {
            description: "Use NIO for large size file",
            before: `import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileReadExample1 {
    public static void main(String[] args) {
        try {
            FileInputStream fileInputStream = new FileInputStream("파일경로");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line); // 파일에서 한 줄씩 읽어 화면에 출력
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}`,
            after: `import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileReadExample3 {
    public static void main(String[] args) {
        try {
            List<String> lines = Files.readAllLines(Paths.get("파일경로"), StandardCharsets.UTF_8);
            for (String line : lines) {
                System.out.println(line); // 파일에서 한 줄씩 읽어 화면에 출력
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
`
        },
        Pattern11: {
            description: "Comparator vs sorted",
            before: `import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

public class SortBefore {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 30));
        people.add(new Person("Charlie", 20));

        // 나이순으로 정렬
        Collections.sort(people, new Comparator<Person>() {
            @Override
            public int compare(Person p1, Person p2) {
                return Integer.compare(p1.getAge(), p2.getAge());
            }
        });

        // 정렬된 결과 출력
        for (Person person : people) {
            System.out.println(person.getName() + " - " + person.getAge());
        }
    }
}`,
            after: `import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.stream.Collectors;

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

public class SortAfter {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person("Alice", 25));
        people.add(new Person("Bob", 30));
        people.add(new Person("Charlie", 20));

        // 나이순으로 정렬하여 출력
        List<Person> sortedPeople = people.stream()
                                          .sorted(Comparator.comparingInt(Person::getAge))
                                          .collect(Collectors.toList());

        // 정렬된 결과 출력
        sortedPeople.forEach(person -> System.out.println(person.getName() + " - " + person.getAge()));
    }
}`
        },
        Pattern12: {
            description: "Use BufferReader",
            before: `public class StringConcatenationExample {
    public static void main(String[] args) {
        // 사용할 문자열들
        String[] words = {"Hello", " ", "World", "!"};

        // String을 사용한 경우
        String concatenatedString = "";
        for (String word : words) {
            concatenatedString += word;
        }
        System.out.println("String Concatenation: " + concatenatedString);
    }
}`,
            after: `public class StringBufferExample {
    public static void main(String[] args) {
        // 사용할 문자열들
        String[] words = {"Hello", " ", "World", "!"};

        // StringBuffer를 사용한 경우
        StringBuffer stringBuffer = new StringBuffer();
        for (String word : words) {
            stringBuffer.append(word);
        }
        String result = stringBuffer.toString();
        System.out.println("StringBuffer Concatenation: " + result);
    }
}
`
        },
        // 다른 패턴들에 대한 설명 추가
    };

    // 대화 상자를 여는 함수
    const handleClickOpen = (pattern) => {
        setCurrentPattern(patternDescriptions[pattern]);
        setOpen(true);
    };

    // 대화 상자를 닫는 함수
    const handleClose = () => {
        setOpen(false);
    };


    useEffect(() => {
        const fetchRefactoredCode = async () => {
            if (!code) return;  // 코드가 없다면 요청을 보내지 않음

            setIsLoading(true);  // 로딩 시작
            try {
                const response = await fetch('/refactoring', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'text/plain',
                    },
                    body: code
                });

                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const data = await response.json();
                setRefactoredCode(data);  // 변환된 코드 상태 업데이트

                const filteredPatterns = Object.entries(data)
                    .filter(([key, value]) => key.startsWith('isPattern') && value)
                    .map(([key, _]) => key.replace('isPattern', 'Pattern'));
                setPatterns(filteredPatterns);

                /* for test
                const test1 = `public class Code { public static void main(String[] args) { int sum = 0; for (int i = 1; i <= 100000; i++) { sum += i * (i - 1); } try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); } System.out.println(sum); } }`;
                const test2 = `public class Code { public static void main(String[] args) { int sum = 0; for (int i = 1; i <= 100000; i++) { sum += i * (i - 1); } try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); } System.out.println(sum); } }`;
                */
                const compareResponse = await fetch('/compare', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',  // headers 수정
                    },
                    /*
                    body: JSON.stringify({
                        "inputCode": test1,
                        "outputCode": test2
                    })
                        */
                    
                    body: JSON.stringify({
                        "inputCode": code,
                        "outputCode": data.code
                    })
                });

                if (!compareResponse.ok) {
                    throw new Error('Failed to fetch comparison data');
                }

                setCompareData(await compareResponse.json());


            } catch (error) {
                console.error('Failed to fetch refactored code:', error);
                setRefactoredCode("Error fetching refactored code.");
            } finally {
                setIsLoading(false);  // 로딩 상태 해제
            }
        };

        fetchRefactoredCode();
    }, [code]);  // code가 변경될 때마다 요청




    return (
        <Section style={{ display: "flex", justifyContent: "center", alignItems: "center", width: "1000px", minHeight: "600px", margin: "20px auto" }}>
            <div style={{ flex: 1, minHeight: "600px", backgroundColor: "#E6F7FF", padding: "20px", fontSize: "16px", fontFamily: "Arial, sans-serif", alignItems: "start" }}>
                <h2 style={{ textAlign: "center", fontWeight: "bold" }}>시스템 정보</h2>    {/* 서버 시스템 정보가 static 하므로 하드코딩 */}
                <p style={{ fontWeight: "bold" }}>CPU</p>
                <ul>
                    <li>Intel Core i7-10700K</li>
                    <li>8코어</li>
                    <li>16스레드</li>
                    <li>기본 클럭 속도: 3.8 GHz</li>
                    <li>최대 클럭 속도: 5.1 GHz</li>
                </ul>
                <p style={{ fontWeight: "bold" }}>Memory</p>
                <ul>
                    <li>32GB</li>
                    <li>DDR4</li>
                    <li>3200MHz</li>
                </ul>
                <p style={{ fontWeight: "bold" }}>GPU</p>
                <ul>
                    <li>GeForce RTX 3080</li>
                    <li>10GB GDDR6X</li>
                    <li>CUDA 코어 수: 8704개</li>
                    <li>부스트 클럭: 1710 MHz</li>
                    <li>메모리 속도: 19 Gbps</li>
                </ul>
                <p style={{ fontWeight: "bold" }}>Power</p>
                <ul>
                    <li>전력 용량: 850W</li>
                </ul>
            </div>
            <div style={{ width: "30px", backgroundColor: "white" }}></div>
            <div style={{ flex: 3, minHeight: "600px", backgroundColor: "#E6F7FF", padding: "20px", display: "flex", flexDirection: "column", alignItems: "center" }}>
                <h2>Analysis</h2>
                <h3>적용된 그린알고리즘 패턴</h3>
                <StyledTable>
                    <TableBody>
                        {patterns.map((pattern, index) => (
                            <TableRow key={index}>
                                <CenteredTableCell textAlign="center">{pattern}</CenteredTableCell>
                                <ButtonContainer textAlign="center">
                                    <Button variant="contained" onClick={() => handleClickOpen(pattern)}>설명</Button>
                                </ButtonContainer>
                            </TableRow>
                        ))}
                    </TableBody>
                </StyledTable>
                <h3>탄소배출량 분석</h3>
                {compareData && (
                    <StyledTable aria-label="customized table">
                        <TableBody>
                            <TableRow>
                                <CenteredTableCell></CenteredTableCell>
                                <CenteredTableCell>
                                    <ImageCell>
                                        <img src="/analysis1.png" alt="탄소 배출량" width="100" />
                                    </ImageCell>
                                    <h3>탄소배출량</h3>
                                </CenteredTableCell>
                                <CenteredTableCell>
                                    <ImageCell>
                                        <img src="/analysis2.png" alt="차로 이동할 수 있는 거리" width="100" />
                                    </ImageCell>
                                    <h3>차로 이동할 수 있는 거리</h3>
                                </CenteredTableCell>
                            </TableRow>
                            <TableRow>
                                <CenteredTableCell>Before</CenteredTableCell>
                                <CenteredTableCell>{compareData?.inputCarbonEmissions}</CenteredTableCell>
                                <CenteredTableCell>{compareData?.carBefore + "km"}</CenteredTableCell>
                            </TableRow>
                            <TableRow>
                                <CenteredTableCell>After</CenteredTableCell>
                                <CenteredTableCell>{compareData?.outputCarbonEmissions}</CenteredTableCell>
                                <CenteredTableCell>{compareData?.carAfter + "km"}</CenteredTableCell>
                            </TableRow>
                        </TableBody>
                    </StyledTable>
                )}
                <PatternDialog
                    open={open}
                    handleClose={handleClose}
                    pattern={currentPattern}
                />
            </div>
        </Section>
    );
};

export default MainBottomPage;