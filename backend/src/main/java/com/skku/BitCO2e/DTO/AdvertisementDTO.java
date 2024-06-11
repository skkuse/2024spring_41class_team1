package com.skku.BitCO2e.DTO;

import com.skku.BitCO2e.model.Advertisement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDTO {
    private String id;
    private String userId;
    private Long usedBit;
    private String message;
    private String imageUrl;
    private String status;
    private String date;

    public AdvertisementDTO(Advertisement ad) {
        userId = ad.getUserId();
        usedBit = ad.getUsedBit();
        message = ad.getMessage();
        imageUrl = ad.getImageUrl();
        status = ad.getStatus();
        date = ad.getDate();
    }
}
