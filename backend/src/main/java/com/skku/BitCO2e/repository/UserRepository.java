package com.skku.BitCO2e.repository;

import com.skku.BitCO2e.model.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface UserRepository {
    CompletableFuture<Void> save(User user);
    CompletableFuture<User> findByEmail(String email);
    CompletableFuture<List<User>> findAll();
}
