package com.skku.BitCO2e.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
public class AdvertisementDTO {
    private String username;
    private Integer current_bit;
    private Integer used_bit;
    private String message;
    private MultipartFile img;
}
