package com.skku.BitCO2e;

import com.skku.BitCO2e.repository.FirebaseUserRepository;
import com.skku.BitCO2e.repository.UserRepository;
import com.skku.BitCO2e.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }

    @Bean
    public UserRepository userRepository() {
        return new FirebaseUserRepository();
    }
}
