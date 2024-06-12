package com.skku.BitCO2e.service;

import com.skku.BitCO2e.DTO.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BitService {
    private final UserService userService;

    @Autowired
    public BitService(UserService userService) {
        this.userService = userService;
    }

    public void addBits(String userId, long bitsToAdd) {
        UserDTO user = userService.findUser(userId);
        long currentBit = user.getBit().getCurrent_bit();

        user.getBit().setCurrent_bit(currentBit + bitsToAdd);
        userService.updateUser(user);
    }

    public void subtractBits(String userId, long bitsToSubtract) {
        UserDTO user = userService.findUser(userId);
        long currentBit = user.getBit().getCurrent_bit();

        if (currentBit > bitsToSubtract) {
            currentBit -= bitsToSubtract;
            user.getBit().setCurrent_bit(currentBit);
            userService.updateUser(user);
        } else {
            throw new RuntimeException("Attempted to subtract more bits than available bits.");
        }
    }

}
