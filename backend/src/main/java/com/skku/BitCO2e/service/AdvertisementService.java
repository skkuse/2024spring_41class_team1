package com.skku.BitCO2e.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.grpc.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class AdvertisementService {

    public void createAdvertisement(String username, String currentBit, String usedBit, String message, String imageUrl) {
        DatabaseReference adRef = FirebaseDatabase.getInstance().getReference("advertisements");

        String adId = adRef.push().getKey();

        Map<String, Object> adData = new HashMap<>();
        adData.put("username", username);
        adData.put("current_bit", currentBit);
        adData.put("used_bit", usedBit);
        adData.put("message", message);
        adData.put("imageUrl", imageUrl);
        adData.put("status", "applied");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        adData.put("date", formattedDate);

        adRef.child(adId).setValueAsync(adData);
    }

    private final Bucket storageBucket;

    public AdvertisementService() {
        this.storageBucket = StorageClient.getInstance().bucket();
    }

    public String uploadAdFile(MultipartFile file) throws IOException {

        // Generate a unique filename
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Define the path to the file in Firebase Storage
        String storagePath = "images/" + filename;

        // Convert MultipartFile to InputStream
        InputStream inputStream = file.getInputStream();

        storageBucket.create(storagePath, inputStream);

        // Return the path to the uploaded file
        return storagePath;
    }

    public boolean updateAdvertisement(String adId, String status) {
        DatabaseReference adRef = FirebaseDatabase.getInstance().getReference("advertisements").child(adId);

        // Check if the advertisement exists
        if (adRef.getKey() == null) {
            return false; // Advertisement not found
        }

        // Update the status field
        adRef.child("status").setValueAsync(status);

        return true; // Advertisement updated successfully
    }

}
