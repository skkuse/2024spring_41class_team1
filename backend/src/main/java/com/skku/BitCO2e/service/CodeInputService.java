package com.skku.BitCO2e.service;

import com.skku.BitCO2e.DTO.AnalyzeRequestDTO;
import com.skku.BitCO2e.DTO.AnalyzeResponseDTO;
import org.springframework.stereotype.Service;

import org.springframework.stereotype.Service;
import javax.tools.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CodeInputService {

    public AnalyzeResponseDTO compareCarbonEmissions(AnalyzeRequestDTO request) throws Exception {
        String inputCode = request.inputCode();
        String outputCode = request.outputCode();

        double emissions1 = calculateCarbonEmissions(inputCode);
        double emissions2 = calculateCarbonEmissions(outputCode);

        if (emissions1 == -1 || emissions2 == -1) {
            throw new Exception("Carbon emissions calculation failed.");
        }

        int reducedPercentage = (int) ((emissions1 - emissions2) / emissions1 * 100);

        // car (km)
        double carBefore = emissions1 / 251;
        carBefore = Math.round(carBefore * 10000) / 10000.0;

        double carAfter = emissions2 / 251;
        carAfter = Math.round(carAfter * 10000) / 10000.0;

        return new AnalyzeResponseDTO(emissions1, emissions2, reducedPercentage, carBefore, carAfter);
    }

    private double calculateCarbonEmissions(String code) throws Exception {
        String codeInfo = CodeInfo(code);

        if (codeInfo == null) {
            System.out.println("Compilation failed.");
            return -1;
        }

        // 소스 코드를 파일로 저장
        File sourceFile = new File("Code.java");
        try (PrintWriter writer = new PrintWriter(sourceFile)) {
            writer.println(codeInfo);
        }

        // Compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile));
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, compilationUnits);
        boolean success = task.call();

        if (!success) {
            for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                System.out.format("Error on line %d%n", diagnostic.getLineNumber());
            }
            System.out.println("Compilation failed.");
            return -1;
        }

        // 실행
        Process process = Runtime.getRuntime().exec("java Code");

        // 타임아웃 초과 시 error
        final int TIMEOUT = 10000;
        try {
            if (!process.waitFor(TIMEOUT, TimeUnit.MILLISECONDS)) {
                process.destroy();
                System.out.println("Execution timed out.");
                return -1;
            }
        } catch (InterruptedException e) {
            System.out.println("Execution interrupted.");
            return -1;
        }

        String data = readOutputFile();

        if (data == null) {
            return -1;
        }

        System.out.println("Output data: " + data);

        // Runtime 값 추출
        String runtimeSubstring = extractValue(data, "Runtime: ");
        double runtime = Double.parseDouble(runtimeSubstring);
        System.out.println("Runtime: " + runtime);

        // Used Memory 값 추출
        String memorySubstring = extractValue(data, "Used Memory: ");
        int memory = Integer.parseInt(memorySubstring);
        System.out.println("Used Memory: " + memory);

        //PUE, PSF 값 설정
        double PUE = 1.67;
        int PSF = 1;

        //Core i7-10700K 가정, core 개수 8개, core power 15.6 (W) (usage = 1)
        int n_CPUcores = 8;
        double CPUpower = 15.6;
        int usageCPU_used = 1;

        //memoryPower (W/GB)
        double memoryPower = 0.3725;

        //carbonIntensity (Korea)
        double carbonIntensity = 415.6;

        //powerNeeded 계산 (W)
        double powerNeeded_core = PUE * n_CPUcores * CPUpower * usageCPU_used; //GPU는 사용 X
        double powerNeeded_memory = PUE * (memory / Math.pow(1024, 3) * memoryPower);
        double powerNeeded = powerNeeded_core + powerNeeded_memory;

        //energyNeeded 계산 (W -> kWh)
        double energyNeeded_core = runtime * powerNeeded_core * PSF / 1000;
        double energyNeeded_memory = runtime * powerNeeded_memory * PSF / 1000;
        double energyNeeded = runtime * powerNeeded * PSF / 1000;

        //carbonEmissions 계산 (gCO2)
        double carbonEmissions = energyNeeded * carbonIntensity;

        // rounding
        carbonEmissions = Math.round(carbonEmissions * 10000) / 10000.0;

        System.out.println("Carbon Emissions: " + carbonEmissions);

        return carbonEmissions;
    }

    private String readOutputFile() {
        StringBuilder data = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("output.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return data.toString();
    }

    // main 확인 및 runtime, usedMemory 등 리소스 사용량 측정용 함수
    private static String CodeInfo(String code) {
        if (!isValidJavaCode(code)) {
            return null; // Invalid Java code
        }

        String oldClassName = code.substring(code.indexOf("class") + 6, code.indexOf("{")).trim(); // 기존 클래스명 추출
        code = code.replace(oldClassName, "Code"); // 클래스명 교체

        int index = code.indexOf("public static void main");

        if (index == -1) {
            return null; // main 함수가 없습니다.
        }

        int braceOpenIndex = code.indexOf("{", index);
        if (braceOpenIndex == -1) {
            return null; // main 함수의 시작 중괄호 `{`가 없습니다.
        }

        int braceCount = 1;
        int braceCloseIndex = braceOpenIndex + 1;
        while (braceCount > 0 && braceCloseIndex < code.length()) {
            if (code.charAt(braceCloseIndex) == '{') {
                braceCount++;
            } else if (code.charAt(braceCloseIndex) == '}') {
                braceCount--;
            }
            braceCloseIndex++;
        }

        if (braceCount != 0) {
            return null; // main 함수의 시작 중괄호 `{`와 끝 중괄호 `}`가 일치하지 않습니다.
        }

        // 리소스 사용량 측정하는 import 문 추가
        String importLib = "import java.io.*; ";
        String frontOfTheMain = "long startTime = System.currentTimeMillis();";
        String BackOfTheMain = "long endTime = System.currentTimeMillis(); Runtime.getRuntime().gc(); long usedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(); String form = String.format(\"/Runtime: %f/Used Memory: %d\", (endTime - startTime) / 1000.0, usedMem); try (FileWriter writer = new FileWriter(\"output.txt\")) { writer.write(form); } catch (IOException e) { e.printStackTrace(); }";

        // main 함수의 시작 중괄호 다음과 끝 중괄호 앞에 리소스 사용량 측정 문자열 code에 추가
        StringBuilder sb = new StringBuilder(code);
        sb.insert(braceOpenIndex + 1, frontOfTheMain);
        sb.insert(braceCloseIndex + frontOfTheMain.length() - 1, BackOfTheMain);

        return importLib + sb.toString();
    }

    // 리소스 측정량 추출용 함수
    private static String extractValue(String input, String key) {
        int startIndex = input.indexOf(key) + key.length();
        int endIndex = input.indexOf("/", startIndex);
        if (endIndex == -1) {
            endIndex = input.length();
        }
        return input.substring(startIndex, endIndex).trim();
    }

    // main 함수 있는지 확인
    private static boolean isValidJavaCode(String code) {
        return code.contains("class") && code.contains("{") && code.contains("public static void main");
    }
}
