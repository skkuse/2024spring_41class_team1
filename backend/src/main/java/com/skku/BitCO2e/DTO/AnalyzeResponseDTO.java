package com.skku.BitCO2e.DTO;

public record AnalyzeResponseDTO(
        Double inputCarbonEmissions,
        Double outputCarbonEmissions,
        int reducedPercentage,
        Double carBefore,
        Double carAfter

) {
}
