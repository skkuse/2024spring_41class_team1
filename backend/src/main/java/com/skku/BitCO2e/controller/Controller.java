package com.skku.BitCO2e.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skku.BitCO2e.DTO.ReviewDTO;
import com.skku.BitCO2e.DTO.UserRegisterDTO;
import com.skku.BitCO2e.model.Advertisement;
import com.skku.BitCO2e.model.User;
import com.skku.BitCO2e.patterns.Pattern1;
import com.skku.BitCO2e.patterns.Pattern2;
import com.skku.BitCO2e.patterns.Pattern3;
import com.skku.BitCO2e.service.AdvertisementService;
import com.skku.BitCO2e.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class Controller {
    private final UserService userService;
    private final AdvertisementService advertisementService;

    private final Pattern1 pattern1;
    private final Pattern2 pattern2;
    private final Pattern3 pattern3;

    @Autowired
    public Controller(UserService userService, AdvertisementService advertisementService) {
        this.userService = userService;
        this.advertisementService = advertisementService;

        this.pattern1 = new Pattern1();
        this.pattern2 = new Pattern2();
        this.pattern3 = new Pattern3();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(UserRegisterDTO userRegisterDTO) {
        userService.createUser(userRegisterDTO);
        return ResponseEntity.ok("User signed up successfully.");
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

    @PostMapping("/advertisements")
    public ResponseEntity<Object> requestAd(
            @RequestParam String username,
            @RequestParam String current_bit,
            @RequestParam String used_bit,
            @RequestParam String message,
            @RequestParam MultipartFile image) {

        CompletableFuture<ResponseEntity<Object>> responseFuture = new CompletableFuture<>();

        if (username.isEmpty() || current_bit.isEmpty() || used_bit.isEmpty() || message.isEmpty() || image.isEmpty()) {
            return new ResponseEntity<>("Missing required field", HttpStatus.BAD_REQUEST);
        }

//        if (!userService.validateUser(username)) {
//            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
//        }

        if (image.getSize() > 10 * 1024 * 1024) {
            return new ResponseEntity<>("Image file size exceeds the limit", HttpStatus.PAYLOAD_TOO_LARGE);
        }

        try {
            // Upload image to Firebase Storage
            String imageUrl = advertisementService.uploadAdFile(image);

            // Save advertisement data in Firebase Realtime Database
            advertisementService.createAdvertisement(username, current_bit, used_bit, message, imageUrl);

            // Update user's bit points
            int currentBits = Integer.parseInt(current_bit);
            int usedBits = Integer.parseInt(used_bit);
            int newBitValue = currentBits - usedBits;
//            userService.updateUserBits(username, newBitValue);

            Map<String, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("currentBit", current_bit);
            response.put("usedBit", used_bit);
            response.put("message", message);
            response.put("imageUrl", imageUrl);
            response.put("status", "applied");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/advertisements")
    public ResponseEntity<Object> getAdvertisementsByStatus(@RequestParam String status) {
        if (!status.equals("applied") && !status.equals("approved")) {
            return new ResponseEntity<>("Invalid status parameter", HttpStatus.BAD_REQUEST);
        }

        try {
            List<Advertisement> advertisements = advertisementService.getAdvertisementsByStatus(status);
            return new ResponseEntity<>(advertisements, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/review")
    public ResponseEntity<String> reviewAd(
            @RequestParam(required = false) String adId,
            @RequestBody ReviewDTO reviewRequest) {

        String status = reviewRequest.getStatus();

        // Check if adId is missing
        if (adId == null || adId.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing required query parameter 'adId'.");
        }

        // Validate status
        if (!status.equals("approved") && !status.equals("rejected")) {
            return ResponseEntity.badRequest().body("Invalid status. Status must be either 'approved' or 'rejected'.");
        }

        try {
            // Update advertisement status
            boolean updated = advertisementService.updateAdvertisement(adId, status);
            if (updated) {
                return ResponseEntity.ok("Application reviewed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found");
            }
        } catch (Exception e) {
            // Handle internal server error (500)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }

}
