package com.skku.BitCO2e.repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skku.BitCO2e.DTO.UserDTO;
import com.skku.BitCO2e.model.Advertisement;
import com.skku.BitCO2e.model.Bit;
import com.skku.BitCO2e.model.Tree;
import com.skku.BitCO2e.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

class AdvertisementRepositoryTest {
    private DatabaseReference adsRef;
    private DatabaseReference usersRef;

    @BeforeEach
    public void setUp() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream("src/main/resources/bitco2e-firebase-adminsdk-6oosw-cadaa2e508.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://bitco2e-default-rtdb.asia-southeast1.firebasedatabase.app")
                .setStorageBucket("bitco2e.appspot.com")
                .build();
        FirebaseApp.initializeApp(options);

        this.adsRef = FirebaseDatabase.getInstance().getReference("advertisements");
        this.usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

//    @Test
//    public void addAdvertisement() {
//        AdvertisementRepository advertisementRepository = new AdvertisementRepository(adsRef);
//        FirebaseUserRepository userRepository = new FirebaseUserRepository(usersRef);
//
//        UserDTO user = userRepository.findByUsername("testuser").join();
//        Advertisement ad = new Advertisement();
//        ad.setUserId(user.getId());
//        ad.setUsedBit(30L);
//        ad.setMessage("test message");
//        ad.setStatus("approved");
//
//        ad.setDate("2024-06-11");
//
//        advertisementRepository.addAdvertisement(ad);
//    }


//    @Test
//    public void deleteAll(){
//        AdvertisementRepository advertisementRepository = new AdvertisementRepository(adsRef);
//        advertisementRepository.deleteAll().join();
//    }

}
