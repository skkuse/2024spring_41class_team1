package com.skku.BitCO2e.service;

import com.google.cloud.storage.*;
import com.google.firebase.cloud.StorageClient;
import com.skku.BitCO2e.DTO.*;
import com.skku.BitCO2e.exceptions.InsufficientBitsException;
import com.skku.BitCO2e.model.Advertisement;
import com.skku.BitCO2e.repository.AdvertisementRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

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
            imageUrl = uploadAdFile(adRequestDTO.getImage());
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
        AdvertisementDTO ad = adRepository.findById(adId).join();
        return convertAdvertisementImageUrl(ad);
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

    public List<AdvertisementInfoDTO> findAdvertisementsByStatus(String status) {
        List<AdvertisementDTO> advertisements;
        if (status.equals("approved")) {
            LocalDate date = LocalDate.now().minusDays(1);
            //개발 편의를 위해 임시로 비활성화
//            advertisements = adRepository.findAllByStatusAndDate(status, date).join();
            advertisements = adRepository.findAllByStatus(status).join();
        } else {
            advertisements = adRepository.findAllByStatus(status).join();
        }

        return enrichAdvertisementsWithUser(convertAdvertisementsImageUrl(advertisements));
    }

    public String uploadAdFile(MultipartFile file) throws IOException {
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String storagePath = "images/" + filename;
        try (InputStream inputStream = file.getInputStream()) {
            storageBucket.create(storagePath, inputStream);
        }

        return storagePath;
    }

    public String getDownloadUrl(String storagePath) {
        Blob blob = storageBucket.get(storagePath);
//        return blob.getMediaLink();
        URL signedUrl = blob.signUrl(15, TimeUnit.MINUTES);
        return signedUrl.toString();
    }

    public AdvertisementDTO convertAdvertisementImageUrl(AdvertisementDTO ad){
        ad.setImageUrl(getDownloadUrl(ad.getImageUrl()));
        return ad;
    }

    public List<AdvertisementDTO> convertAdvertisementsImageUrl(List<AdvertisementDTO> ads){
        List<AdvertisementDTO> newAds = new ArrayList<>();
        for(AdvertisementDTO ad : ads){
            newAds.add(convertAdvertisementImageUrl(ad));
        }

        return newAds;
    }

    public AdvertisementInfoDTO enrichAdvertisementWithUser(AdvertisementDTO ad){
        AdvertisementInfoDTO adInfo = new AdvertisementInfoDTO(ad);
        String userId = ad.getUserId();
        UserDTO user = userService.findUser(userId);
        adInfo.setUser(new UserInfoDTO(user));
        return adInfo;
    }

    public List<AdvertisementInfoDTO> enrichAdvertisementsWithUser(List<AdvertisementDTO> ads){
        List<AdvertisementInfoDTO> newAds = new ArrayList<>();
        for(AdvertisementDTO ad : ads){
            newAds.add(enrichAdvertisementWithUser(ad));
        }
        return newAds;
    }

}
