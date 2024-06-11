package com.skku.BitCO2e;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;

@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void init(){
        try{
            FileInputStream serviceAccount =
                    new FileInputStream("backend/src/main/resources/bitco2e-firebase-adminsdk-6oosw-cadaa2e508.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://bitco2e-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .setStorageBucket("bitco2e.appspot.com")
                    .build();

            FirebaseApp.initializeApp(options);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Bean
    public Bucket firebaseStorageBucket() {
        return StorageClient.getInstance().bucket();
    }

    @Bean
    public DatabaseReference usersRef(){
        return FirebaseDatabase.getInstance().getReference("users");
    }

    @Bean
    public DatabaseReference adsRef(){
        return FirebaseDatabase.getInstance().getReference("advertisements");
    }
}
