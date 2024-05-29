package com.skku.BitCO2e.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.*;
import com.skku.BitCO2e.model.Advertisement;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

public class AdvertisementService {
    private final Bucket storageBucket;

    public AdvertisementService(Bucket storageBucket) {
        try {
            this.storageBucket = StorageClient.getInstance().bucket();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize Firebase Storage bucket", e);
        }
    }

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(formatter);
        adData.put("date", formattedDate);

        adRef.child(adId).setValueAsync(adData);
    }


    public String uploadAdFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String storagePath = "images/" + filename;
        try (InputStream inputStream = file.getInputStream()) {
            storageBucket.create(storagePath, inputStream);
        }

        return storagePath;
    }

    public boolean updateAdvertisement(String adId, String status) {
        DatabaseReference adRef = FirebaseDatabase.getInstance().getReference("advertisements").child(adId);

        if (adRef.getKey() == null) {
            return false; // Advertisement not found
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(formatter);

        adRef.child("status").setValueAsync(status);
        adRef.child("date").setValueAsync(formattedDate);

        return true;
    }

    public List<Advertisement> getAdvertisementsByStatus(String status) throws InterruptedException {
        List<Advertisement> advertisements = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        DatabaseReference adRef = FirebaseDatabase.getInstance().getReference("advertisements");

        Query query = adRef.orderByChild("status").equalTo(status);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String formattedDate = LocalDateTime.now().format(formatter);
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Advertisement advertisement = snapshot.getValue(Advertisement.class);
                    if (advertisement != null) {
                        if (status.equals("approved")) {
                            if (formattedDate.equals(advertisement.getDate())) {
                                advertisements.add(advertisement);
                            }
                        } else {
                            advertisements.add(advertisement);
                        }
                    }
                }
                latch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                latch.countDown();
            }
        });

        latch.await();
        return advertisements;
    }

}
