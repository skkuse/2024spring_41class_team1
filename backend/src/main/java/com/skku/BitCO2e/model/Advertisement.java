package com.skku.BitCO2e.model;

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
}
