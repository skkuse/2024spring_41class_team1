package com.skku.BitCO2e.repository;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skku.BitCO2e.DTO.UserDTO;
import com.skku.BitCO2e.model.Bit;
import com.skku.BitCO2e.model.Tree;
import com.skku.BitCO2e.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

class UserRepositoryTest {
    private DatabaseReference userRef;

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

        this.userRef = FirebaseDatabase.getInstance().getReference("users");
    }

    @Test
    public void save(){
        FirebaseUserRepository userRepository = new FirebaseUserRepository(userRef);

        User user = new User();
        user.setUsername("Go Byeong San");
        user.setEmail("gobyeongsan@gmail.com");
        user.setPassword("hashedpassword");
        user.setBit(new Bit(0,0));
        user.setTree(new Tree(0,0));

        CompletableFuture<UserDTO> saveFuture = userRepository.save(user);

        UserDTO userDto = saveFuture.join();

        CompletableFuture<UserDTO> getUserFuture = userRepository.findById(userDto.getId());
        UserDTO retrievedUserDto = getUserFuture.join();

        Assertions.assertThat(retrievedUserDto.getId()).isEqualTo(userDto.getId());
    }

    @Test
    public void findByEmail(){
        FirebaseUserRepository userRepository = new FirebaseUserRepository(userRef);

        User user = new User();
        user.setUsername("Go Byeong San2");
        user.setEmail("gobyeongsan3@gmail.com");
        user.setPassword("hashedpassword2");
        user.setBit(new Bit(0,0));
        user.setTree(new Tree(0,0));

        CompletableFuture<UserDTO> saveFuture = userRepository.save(user);

        UserDTO userDto = saveFuture.join();

        CompletableFuture<UserDTO> getUserFuture = userRepository.findByEmail(user.getEmail());
        UserDTO retrievedUserDto = getUserFuture.join();
        System.out.println(retrievedUserDto.getEmail());
        System.out.println(userDto.getEmail());
        Assertions.assertThat(retrievedUserDto.getId()).isEqualTo(userDto.getId());
    }

//    @Test
//    public void update(){
//        FirebaseUserRepository userRepository = new FirebaseUserRepository(userRef);
//
//        UserDTO user = userRepository.findByUsername("testuser").join();
//
//        User newUser = new User();
//        newUser.setUsername(user.getUsername());
//        newUser.setEmail(user.getEmail());
//        newUser.setPassword(user.getPassword());
//        newUser.setBit(new Bit(200000,200000));
//        newUser.setTree(new Tree(0,0));
//
//        userRepository.update(user.getId(), newUser).join();
//    }

    @Test
    public void delete(){
        FirebaseUserRepository userRepository = new FirebaseUserRepository(userRef);

        CompletableFuture<UserDTO> getUserFuture = userRepository.findByUsername("admin");
        UserDTO retrievedUserDto = getUserFuture.join();
        System.out.println(retrievedUserDto);
        String userId = retrievedUserDto.getId();

        userRepository.delete(userId).join();
//        userRepository.delete("-NyxxW4jBote4p3sgPfM").join();
    }

}
