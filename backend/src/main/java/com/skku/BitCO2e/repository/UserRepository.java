package com.skku.BitCO2e.repository;

import com.skku.BitCO2e.DTO.UserDTO;
import com.skku.BitCO2e.model.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserRepository {
    CompletableFuture<UserDTO> save(User user);
    CompletableFuture<UserDTO> findById(String id);
    CompletableFuture<UserDTO> findByEmail(String email);
    CompletableFuture<UserDTO> findByUsername(String username);
    CompletableFuture<UserDTO> update(String id, User user);
    CompletableFuture<List<UserDTO>> findAll();
    CompletableFuture<Void> delete(String id);
    CompletableFuture<List<UserDTO>> findTopByBits(Integer limit);
}
