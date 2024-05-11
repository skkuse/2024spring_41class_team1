package com.skku.BitCO2e.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.skku.BitCO2e.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public void createUser(User user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.push().setValueAsync(user);
    }
}
