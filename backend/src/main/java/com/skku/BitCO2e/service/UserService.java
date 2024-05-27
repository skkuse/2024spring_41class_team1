package com.skku.BitCO2e.service;

import com.skku.BitCO2e.DTO.UserRegisterDTO;
import com.skku.BitCO2e.model.User;
import com.skku.BitCO2e.repository.FirebaseUserRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@Service
public class UserService {

    private FirebaseUserRepository userRepository;

    public CompletableFuture<Boolean> createUser(UserRegisterDTO userRegisterDTO) {
        User user = new User();

        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(userRegisterDTO.getPassword());

        CompletableFuture<Void> future = userRepository.save(user);



        return future.thenApply(__ -> {
            System.out.println("User creation completed successfully.");
            return true;
        }).exceptionally(ex -> {
            System.err.println("User creation failed: " + ex);
            return false;
        });
    }
}
