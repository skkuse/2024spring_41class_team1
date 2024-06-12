package com.skku.BitCO2e;

import com.google.cloud.storage.Bucket;
import com.google.firebase.database.DatabaseReference;
import com.skku.BitCO2e.repository.AdvertisementRepository;
import com.skku.BitCO2e.repository.FirebaseUserRepository;
import com.skku.BitCO2e.repository.UserRepository;
import com.skku.BitCO2e.service.AdvertisementService;
import com.skku.BitCO2e.service.BitService;
import com.skku.BitCO2e.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

    @Bean
    public UserService userService(UserRepository userRepository) {
        return new UserService(userRepository);
    }

    @Bean
    public UserRepository userRepository(DatabaseReference usersRef) {
        return new FirebaseUserRepository(usersRef);
    }

    @Bean
    public AdvertisementService advertisementService(Bucket firebaseStorageBucket, AdvertisementRepository advertisementRepository, UserService userService, BitService bitService) {
        return new AdvertisementService(firebaseStorageBucket, advertisementRepository, userService, bitService);
    }

    @Bean
    AdvertisementRepository advertisementRepository(DatabaseReference adsRef) {
        return new AdvertisementRepository(adsRef);
    }
}
