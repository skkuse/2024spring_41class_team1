package com.skku.BitCO2e.DTO;

import com.skku.BitCO2e.model.Bit;
import com.skku.BitCO2e.model.Tree;
import com.skku.BitCO2e.security.UserDetailsImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class UserSessionDTO {
    private String id;
    private String username;
    private String email;
    private Collection authorities;
    private Bit bit;
    private Tree tree;

    public UserSessionDTO(UserDetailsImpl userDetails) {
        UserDTO userDTO = userDetails.getUserDTO();
        id = userDTO.getId();
        username = userDTO.getUsername();
        email = userDTO.getEmail();
        authorities = userDetails.getAuthorities();
        bit = userDTO.getBit();
        tree = userDTO.getTree();
    }

    public UserSessionDTO(UserDTO userDTO) {
        id = userDTO.getId();
        username = userDTO.getUsername();
        email = userDTO.getEmail();
        bit = userDTO.getBit();
        tree = userDTO.getTree();
    }
}
