package com.skku.BitCO2e.DTO;

import com.skku.BitCO2e.model.Advertisement;
import com.skku.BitCO2e.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementInfoDTO {
    private String id;
    private UserInfoDTO user;
    private Long usedBit;
    private String message;
    private String imageUrl;
    private String status;
    private String date;

    public AdvertisementInfoDTO(Advertisement ad) {
        usedBit = ad.getUsedBit();
        message = ad.getMessage();
        imageUrl = ad.getImageUrl();
        status = ad.getStatus();
        date = ad.getDate();
    }

    public AdvertisementInfoDTO(AdvertisementDTO ad) {
        id = ad.getId();
        usedBit = ad.getUsedBit();
        message = ad.getMessage();
        imageUrl = ad.getImageUrl();
        status = ad.getStatus();
        date = ad.getDate();
    }
}
