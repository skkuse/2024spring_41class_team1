package com.skku.BitCO2e.service;

import com.google.cloud.storage.*;
import com.skku.BitCO2e.DTO.AdvertisementDTO;
import com.skku.BitCO2e.DTO.AdvertisementRequestDTO;
import com.skku.BitCO2e.DTO.ReviewDTO;
import com.skku.BitCO2e.DTO.UserDTO;
import com.skku.BitCO2e.exceptions.InsufficientBitsException;
import com.skku.BitCO2e.model.Advertisement;
import com.skku.BitCO2e.repository.AdvertisementRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdvertisementService {
    private final Bucket storageBucket;
    private final AdvertisementRepository adRepository;
    private final UserService userService;
    private final BitService bitService;

    public AdvertisementService(Bucket storageBucket, AdvertisementRepository advertisementRepository, UserService userService, BitService bitService) {
        this.storageBucket = storageBucket;
        this.adRepository = advertisementRepository;
        this.userService = userService;
        this.bitService = bitService;
    }

    public void createAdvertisement(String userId, AdvertisementRequestDTO adRequestDTO) {
        UserDTO user = userService.findUser(userId);
        long usedBit = 50L;

        if (user.getBit().getCurrent_bit() < usedBit) {
            throw new InsufficientBitsException("User does not have enough bits");
        }

        String message = adRequestDTO.getMessage();
        String imageUrl;
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

    public AdvertisementDTO findAdvertisement(String adId) {
        return adRepository.findById(adId).join();
    }


    public String uploadAdFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String storagePath = "images/" + filename;
        try (InputStream inputStream = file.getInputStream()) {
            storageBucket.create(storagePath, inputStream);
        }

        return storagePath;
    }

    public void reviewAdvertisement(ReviewDTO reviewDTO) {
        String adId = reviewDTO.getAdId();
        AdvertisementDTO ad = findAdvertisement(adId);
        ad.setStatus(reviewDTO.getStatus());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(formatter);
        ad.setDate(formattedDate);

        if(reviewDTO.getStatus().equals("approved")){
            bitService.subtractBits(ad.getUserId(), ad.getUsedBit());
        }

        updateAdvertisement(adId, new Advertisement(ad));
    }

    public void updateAdvertisement(String adId, Advertisement ad) {
        adRepository.update(adId, ad).join();
    }

    public List<AdvertisementDTO> getAdvertisementsByStatus(String status) {
        List<AdvertisementDTO> advertisements;
        if (status.equals("approved")) {
            LocalDate date = LocalDate.now().minusDays(1);
            advertisements = adRepository.findAllByStatusAndDate(status, date).join();
        } else {
            advertisements = adRepository.findAllByStatus(status).join();
        }
        return advertisements;
    }

}
