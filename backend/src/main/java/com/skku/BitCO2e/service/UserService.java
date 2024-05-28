package com.skku.BitCO2e.service;

import com.skku.BitCO2e.DTO.UserDTO;
import com.skku.BitCO2e.DTO.UserRegisterDTO;
import com.skku.BitCO2e.model.User;
import com.skku.BitCO2e.repository.UserRepository;

import java.util.concurrent.CompletableFuture;

public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CompletableFuture<Boolean> createUser(UserRegisterDTO userRegisterDTO) {
        User user = new User();

        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(userRegisterDTO.getPassword());

        CompletableFuture<UserDTO> future = userRepository.save(user);



        return future.thenApply(__ -> {
            System.out.println("User creation completed successfully.");
            return true;
        }).exceptionally(ex -> {
            System.err.println("User creation failed: " + ex);
            return false;
        });
    }
}
