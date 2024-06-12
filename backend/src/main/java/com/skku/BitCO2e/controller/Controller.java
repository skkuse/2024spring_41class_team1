package com.skku.BitCO2e.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skku.BitCO2e.DTO.*;
import com.skku.BitCO2e.patterns.Pattern1;
import com.skku.BitCO2e.patterns.Pattern2;
import com.skku.BitCO2e.patterns.Pattern3;
import com.skku.BitCO2e.security.UserDetailsImpl;
import com.skku.BitCO2e.service.AdvertisementService;
import com.skku.BitCO2e.service.BitService;
import com.skku.BitCO2e.service.CodeInputService;
import com.skku.BitCO2e.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class Controller {
    private final UserService userService;
    private final AdvertisementService advertisementService;
    private final BitService bitService;
    private final CodeInputService codeInputService;

    private final Pattern1 pattern1;
    private final Pattern2 pattern2;
    private final Pattern3 pattern3;

    @Autowired
    public Controller(UserService userService, AdvertisementService advertisementService, BitService bitService, CodeInputService codeInputService) {
        this.userService = userService;
        this.advertisementService = advertisementService;
        this.bitService = bitService;
        this.codeInputService = codeInputService;

        this.pattern1 = new Pattern1();
        this.pattern2 = new Pattern2();
        this.pattern3 = new Pattern3();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(UserRegisterDTO userRegisterDTO) {
        userService.createUser(userRegisterDTO);
        return ResponseEntity.ok("User signed up successfully.");
    }

    @GetMapping("/session")
    public UserSessionDTO session(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userService.getUserSession(userDetails);
    }


    @PostMapping("/refactoring")
    public String refactoring(@RequestBody String codeInput) {
        String refactor1 = pattern1.main(codeInput);
        boolean isPattern1 = refactor1.contains("public class Fixed");

        String refactor2 = pattern2.main(refactor1);
        boolean isPattern2 = !refactor1.equals(refactor2);

        String refactor3 = pattern3.main(refactor2);
        boolean isPattern3 = !refactor2.equals(refactor3);

        Map<String, Object> response = new HashMap<>();
        response.put("isPattern1", isPattern1);
        response.put("isPattern2", isPattern2);
        response.put("isPattern3", isPattern3);
        response.put("code", refactor3);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            throw new RuntimeException("Error converting response to JSON", e);
        }
    }

    @PostMapping("/bit")
    public void addBit(@RequestBody String userId) {
        bitService.addBits(userId, 10);
    }

    @PostMapping("/advertisement")
    public ResponseEntity<String> requestAd(@AuthenticationPrincipal UserDetailsImpl userDetails, @Validated AdvertisementRequestDTO advertisementRequestDTO) {
        String userId = userDetails.getUserId();

        if (advertisementRequestDTO.getImg().getSize() > 10 * 1024 * 1024) {
            return new ResponseEntity<>("Image file size exceeds the limit", HttpStatus.PAYLOAD_TOO_LARGE);
        }

        advertisementService.createAdvertisement(userId, advertisementRequestDTO);
        return ResponseEntity.ok("apply advertisement successfully.");
    }

    @GetMapping("/advertisements")
    public ResponseEntity<Object> getAdvertisementsByStatus(@RequestParam String status) {
        if (!status.equals("applied") && !status.equals("approved")) {
            return new ResponseEntity<>("Invalid status parameter", HttpStatus.BAD_REQUEST);
        }

        List<AdvertisementDTO> advertisements = advertisementService.getAdvertisementsByStatus(status);
        return new ResponseEntity<>(advertisements, HttpStatus.OK);
    }

    @PostMapping("/review")
    public ResponseEntity<String> reviewAd(@Validated ReviewDTO reviewDTO) {

        String status = reviewDTO.getStatus();

        // Validate status
        if (!status.equals("approved") && !status.equals("rejected")) {
            return ResponseEntity.badRequest().body("Invalid status. Status must be either 'approved' or 'rejected'.");
        }

        advertisementService.reviewAdvertisement(reviewDTO);
        return ResponseEntity.ok("Review advertisement successfully.");
    }

    @PostMapping("/compare")
    public ResponseEntity<AnalyzeResponseDTO> compareAnalyzeCE(@RequestBody AnalyzeRequestDTO request) {
        try {
            AnalyzeResponseDTO response = codeInputService.compareCarbonEmissions(request);
            if (response.inputCarbonEmissions() == -1 || response.outputCarbonEmissions() == -1) {
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            AnalyzeResponseDTO errorResponse = new AnalyzeResponseDTO((double) -1, (double) -1, -1, (double) -1, (double) -1);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
