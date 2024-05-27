package com.skku.BitCO2e.repository;

import com.google.firebase.database.*;
import com.skku.BitCO2e.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class FirebaseUserRepository implements UserRepository {
    @Override
    public CompletableFuture<Void> save(User user) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        String userId = usersRef.push().getKey();

        usersRef.child(userId).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    future.completeExceptionally(databaseError.toException());
                } else {
                    future.complete(null);
                }
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<User> findByEmail(String email) {
        CompletableFuture<User> future = new CompletableFuture<>();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        Query emailQuery = usersRef.orderByChild("email").equalTo(email);

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    String username = userSnapshot.child("username").getValue(String.class); // user name
                    String userEmail = userSnapshot.child("email").getValue(String.class); // user email
                    String password = userSnapshot.child("password").getValue(String.class); // user hashed password

                    User user = new User(userId, username, userEmail, password);
                    future.complete(user);
                    return; // iterate only once
                }
                future.complete(null); // if no user, return null
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<List<User>> findAll(){
        CompletableFuture<List<User>> future = new CompletableFuture<>();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    String username = userSnapshot.child("username").getValue(String.class); // user name
                    String userEmail = userSnapshot.child("email").getValue(String.class); // user email
                    String password = userSnapshot.child("password").getValue(String.class); // user hashed password

                    User user = new User(userId, username, userEmail, password);
                    users.add(user);
                }
                future.complete(users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }
}
