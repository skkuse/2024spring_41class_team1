package com.skku.BitCO2e.service;

import com.google.firebase.database.*;
import com.skku.BitCO2e.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UserService {

    public void createUser(User user) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        usersRef.push().setValueAsync(user);
    }

    public CompletableFuture<Boolean> validateUser(String username) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query query = usersRef.orderByChild("username").equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                future.complete(dataSnapshot.exists());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }
}
