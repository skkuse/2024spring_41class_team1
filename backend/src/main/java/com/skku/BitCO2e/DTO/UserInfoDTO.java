package com.skku.BitCO2e.DTO;

import com.skku.BitCO2e.model.Bit;
import com.skku.BitCO2e.model.Tree;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private String id;
    private String username;
    private Bit bit;
    private Tree tree;

    public UserInfoDTO(UserDTO userDTO) {
        username = userDTO.getUsername();
        id = userDTO.getId();
        bit = userDTO.getBit();
        tree = userDTO.getTree();
    }
}
