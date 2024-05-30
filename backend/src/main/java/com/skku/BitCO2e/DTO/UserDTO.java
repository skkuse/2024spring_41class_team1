package com.skku.BitCO2e.DTO;

import com.skku.BitCO2e.model.Bit;
import com.skku.BitCO2e.model.Tree;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String id;
    private String username;
    private String email;
    private String password;
    private Bit bit;
    private Tree tree;
}
