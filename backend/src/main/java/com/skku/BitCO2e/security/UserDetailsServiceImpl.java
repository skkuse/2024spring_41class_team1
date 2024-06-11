package com.skku.BitCO2e.security;

import com.skku.BitCO2e.DTO.UserDTO;
import com.skku.BitCO2e.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CompletableFuture<UserDTO> future = userRepository.findByUsername(username);
        System.out.println(username);

        UserDTO userDTO;
        try {
            userDTO = future.get();
            System.out.println(userDTO);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        if(userDTO == null) {
//            System.out.println("User not found!!!!!!");
            throw new UsernameNotFoundException("user not found");
        }
        return new UserDetailsImpl(userDTO);
    }
}
