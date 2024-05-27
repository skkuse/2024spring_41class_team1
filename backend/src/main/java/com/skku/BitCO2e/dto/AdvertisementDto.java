package com.skku.BitCO2e.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdvertisementDto {
    private String username;
    private Integer current_bit;
    private Integer used_bit;
    private String message;
    private MultipartFile img;
}
