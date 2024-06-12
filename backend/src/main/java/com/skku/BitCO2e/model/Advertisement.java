package com.skku.BitCO2e.model;

import com.skku.BitCO2e.DTO.AdvertisementDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Advertisement {
    private String userId;
    private Long usedBit;
    private String message;
    private String imageUrl;
    private String status;
    private String date;

    public Advertisement(AdvertisementDTO adDTO) {
        this.userId = adDTO.getUserId();
        this.usedBit = adDTO.getUsedBit();
        this.message = adDTO.getMessage();
        this.imageUrl = adDTO.getImageUrl();
        this.status = adDTO.getStatus();
        this.date = adDTO.getDate();
    }
}
