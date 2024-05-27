package com.skku.BitCO2e.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginDTO {
    private String email;
    private String password;

    public UserLoginDTO(String username, String email, String password) {
        this.email = email;
        this.password = password;
    }
}
