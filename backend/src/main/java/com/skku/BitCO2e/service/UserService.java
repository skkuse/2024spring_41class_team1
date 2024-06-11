package com.skku.BitCO2e.service;

import com.skku.BitCO2e.DTO.UserDTO;
import com.skku.BitCO2e.DTO.UserRegisterDTO;
import com.skku.BitCO2e.model.Bit;
import com.skku.BitCO2e.model.Tree;
import com.skku.BitCO2e.model.User;
import com.skku.BitCO2e.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserRegisterDTO userRegisterDTO) {
        validateDuplicatedUsername(userRegisterDTO.getUsername());

        User user = new User();
        user.setUsername(userRegisterDTO.getUsername());
        user.setEmail(userRegisterDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));

        user.setBit(new Bit(0,0));
        user.setTree(new Tree(0,0));

        userRepository.save(user).join();
    }

    public void updateUser(UserDTO userDTO) {
        String userId = userDTO.getId();

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setBit(new Bit(0,1));
        user.setTree(new Tree(0,1));

        userRepository.update(userId, user).join();
    }

    public UserDTO findUser(String userId) {
        try {
            return userRepository.findById(userId).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Error occured during find user",e);
        }
    }

    public void validateDuplicatedUsername(String username) {
        CompletableFuture<UserDTO> future = userRepository.findByUsername(username);

        try{
            UserDTO userDTO = future.get();
            if(userDTO != null)
                throw new IllegalStateException("Duplicated email");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new IllegalStateException("Error occured during checking for duplicated username",e);
        }
    }
}
