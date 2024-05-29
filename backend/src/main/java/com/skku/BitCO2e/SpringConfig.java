package com.skku.BitCO2e;

import com.google.cloud.storage.Bucket;
import com.google.firebase.database.DatabaseReference;
import com.skku.BitCO2e.repository.FirebaseUserRepository;
import com.skku.BitCO2e.repository.UserRepository;
import com.skku.BitCO2e.service.AdvertisementService;
import com.skku.BitCO2e.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringConfig {

    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return new UserService(userRepository, passwordEncoder);
    }

    @Bean
    public UserRepository userRepository(DatabaseReference usersRef) {
        return new FirebaseUserRepository(usersRef);
    }

    @Bean
    public AdvertisementService advertisementService(Bucket firebaseStorageBucket) {
        return new AdvertisementService(firebaseStorageBucket);
    }
}
