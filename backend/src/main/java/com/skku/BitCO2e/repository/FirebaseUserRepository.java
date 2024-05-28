package com.skku.BitCO2e.repository;

import com.google.firebase.database.*;
import com.skku.BitCO2e.DTO.UserDTO;
import com.skku.BitCO2e.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FirebaseUserRepository implements UserRepository {
    private DatabaseReference usersRef;

    public FirebaseUserRepository() {
        this.usersRef = FirebaseDatabase.getInstance().getReference("users");
    }

    public FirebaseUserRepository(DatabaseReference usersRef) {
        this.usersRef = usersRef;
    }

    @Override
    public CompletableFuture<UserDTO> save(User user) {
        CompletableFuture<UserDTO> future = new CompletableFuture<>();

        String userId = usersRef.push().getKey();

        usersRef.child(userId).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    future.completeExceptionally(databaseError.toException());
                } else {
                    UserDTO userDTO = userToUserDTO(userId, user);
                    future.complete(userDTO);
                }
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<UserDTO> findById(String userId) {
        CompletableFuture<UserDTO> future = new CompletableFuture<>();

        Query query = usersRef.child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userId = dataSnapshot.getKey();
                User user = dataSnapshot.getValue(User.class);
                UserDTO userDto = userToUserDTO(userId, user);
                future.complete(userDto);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }

    @Override
    public CompletableFuture<UserDTO> findByEmail(String email) {
        CompletableFuture<UserDTO> future = new CompletableFuture<>();

        Query emailQuery = usersRef.orderByChild("email").equalTo(email);

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);
                    UserDTO userDto = userToUserDTO(userId, user);

                    future.complete(userDto);
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
    public CompletableFuture<List<UserDTO>> findAll(){
        CompletableFuture<List<UserDTO>> future = new CompletableFuture<>();

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserDTO> userDtos = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);
                    UserDTO userDto = userToUserDTO(userId, user);

                    userDtos.add(userDto);
                }
                future.complete(userDtos);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<Void> delete(String id) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        usersRef.child(id).removeValue(new DatabaseReference.CompletionListener() {
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

    public UserDTO userSnapshotToUserDTO(DataSnapshot userSnapshot) {
        String userId = userSnapshot.getKey();
        String username = userSnapshot.child("username").getValue(String.class);
        String userEmail = userSnapshot.child("email").getValue(String.class);
        String password = userSnapshot.child("password").getValue(String.class);
        return new UserDTO(userId, username, userEmail, password);
    }
    public UserDTO userToUserDTO(String userId,User user) {
        String username = user.getUsername();
        String userEmail = user.getEmail();
        String password = user.getPassword();
        return new UserDTO(userId, username, userEmail, password);
    }

}
