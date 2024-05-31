package com.skku.BitCO2e.service;

import com.skku.BitCO2e.DTO.AnalyzeRequestDTO;
import com.skku.BitCO2e.DTO.AnalyzeResponseDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CodeInputServiceTest {

    private final CodeInputService codeInputService = new CodeInputService();

    @Test
    public void testCompareCarbonEmissions() throws Exception {
        String inputCode = "public class Code { public static void main(String[] args) { int sum = 0; for (int i = 1; i <= 100000; i++) { sum += i * (i - 1); } try { Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); } System.out.println(sum); } }";
        String outputCode = "public class Code { public static void main(String[] args) { int sum = 0; for (int i = 1; i <= 1000000; i++) { sum += i * (i - 1); } try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); } System.out.println(sum); } }";

        AnalyzeRequestDTO request = new AnalyzeRequestDTO(inputCode, outputCode);
        AnalyzeResponseDTO response = codeInputService.compareCarbonEmissions(request);

        assertNotNull(response);
        assertTrue(response.inputCarbonEmissions() > 0, "Expected input carbon emissions to be greater than 0, but got " + response.inputCarbonEmissions());
        assertTrue(response.outputCarbonEmissions() > 0, "Expected output carbon emissions to be greater than 0, but got " + response.outputCarbonEmissions());
        assertEquals(response.reducedPercentage(), (int) ((response.inputCarbonEmissions() - response.outputCarbonEmissions()) / response.inputCarbonEmissions() * 100));
        assertEquals(response.carBefore(), Math.round(response.inputCarbonEmissions() / 251 * 10000)/10000.0);
        assertEquals(response.carAfter(), Math.round(response.outputCarbonEmissions() / 251 * 10000)/10000.0);
    }
}
