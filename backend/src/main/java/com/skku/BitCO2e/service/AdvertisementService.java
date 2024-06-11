package com.skku.BitCO2e.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.*;
import com.skku.BitCO2e.DTO.AdvertisementDTO;
import com.skku.BitCO2e.DTO.AdvertisementRequestDTO;
import com.skku.BitCO2e.DTO.UserDTO;
import com.skku.BitCO2e.exceptions.InsufficientBitsException;
import com.skku.BitCO2e.model.Advertisement;
import com.skku.BitCO2e.repository.AdvertisementRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;

public class AdvertisementService {
    private final Bucket storageBucket;
    private final AdvertisementRepository adRepository;
    private final UserService userService;

    public AdvertisementService(Bucket storageBucket, AdvertisementRepository advertisementRepository, UserService userService) {
        this.storageBucket = storageBucket;
        this.adRepository = advertisementRepository;
        this.userService = userService;
    }

    public void createAdvertisement(String userId, AdvertisementRequestDTO adRequestDTO) {
        UserDTO user = userService.findUser(userId);
        Long usedBit = 30L;

        if (user.getBit().getCurrent_bit() < usedBit) {
            throw new InsufficientBitsException("User does not have enough bits");
        }

        String message = adRequestDTO.getMessage();
        String imageUrl = null;
        try {
            imageUrl = uploadAdFile(adRequestDTO.getImg());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Advertisement ad = new Advertisement();

        ad.setUserId(userId);
        ad.setUsedBit(usedBit);
        ad.setMessage(message);
        ad.setImageUrl(imageUrl);
        ad.setStatus("applied");
//        ad.setStatus("approved");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(formatter);
        ad.setDate(formattedDate);
//        ad.setDate("2024-06-11");

        adRepository.addAdvertisement(ad).join();
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

    public List<AdvertisementDTO> getAdvertisementsByStatus(String status) {
        List<AdvertisementDTO> advertisements = adRepository.findAllByStatus(status).join();
        return advertisements;
    }

}
