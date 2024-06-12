package com.skku.BitCO2e.repository;

import com.google.firebase.database.*;
import com.skku.BitCO2e.DTO.UserDTO;
import com.skku.BitCO2e.model.Bit;
import com.skku.BitCO2e.model.Tree;
import com.skku.BitCO2e.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FirebaseUserRepository implements UserRepository {
    private final DatabaseReference usersRef;

    public FirebaseUserRepository(DatabaseReference usersRef) {
        this.usersRef = usersRef;
    }

    @Override
    public CompletableFuture<UserDTO> save(User user) {
        CompletableFuture<UserDTO> future = new CompletableFuture<>();

        String userId = usersRef.push().getKey();

        usersRef.child(userId).setValue(user, (databaseError, databaseReference) -> {
            if (databaseError != null) {
                future.completeExceptionally(databaseError.toException());
            } else {
                UserDTO userDTO = userToUserDTO(userId, user);
                future.complete(userDTO);
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
                if (dataSnapshot.exists()) {
                    String userId = dataSnapshot.getKey();
                    User user = dataSnapshot.getValue(User.class);
                    UserDTO userDto = userToUserDTO(userId, user);
                    future.complete(userDto);
                } else{
                    future.completeExceptionally(new RuntimeException("No user found"));
                }
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
                List<UserDTO> userDtoList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);
                    UserDTO userDto = userToUserDTO(userId, user);
                    userDtoList.add(userDto);
                }
                if (!userDtoList.isEmpty()) {
                    future.complete(userDtoList.get(0)); // 반환할 사용자가 여러 명이라면 첫 번째 사용자 반환
                } else {
                    future.complete(null); // 이메일과 일치하는 사용자가 없는 경우 null 반환
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    @Override
    public CompletableFuture<UserDTO> findByUsername(String username) {
        CompletableFuture<UserDTO> future = new CompletableFuture<>();

        Query emailQuery = usersRef.orderByChild("username").equalTo(username);

        emailQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserDTO> userDtoList = new ArrayList<>();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    User user = userSnapshot.getValue(User.class);
                    UserDTO userDto = userToUserDTO(userId, user);
                    userDtoList.add(userDto);
                }
                if (!userDtoList.isEmpty()) {
                    future.complete(userDtoList.get(0)); // 반환할 사용자가 여러 명이라면 첫 번째 사용자 반환
                } else {
                    future.complete(null); // 이메일과 일치하는 사용자가 없는 경우 null 반환
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });

        return future;
    }

    public CompletableFuture<List<UserDTO>> findTopByBits(Integer limit) {
        CompletableFuture<List<UserDTO>> future = new CompletableFuture<>();
        Query query = usersRef.orderByChild("bit/total_bit").limitToLast(limit);

        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<UserDTO> topUsers = new ArrayList<>();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    System.out.println(userSnapshot);
                    if (userSnapshot.exists()) {
                        String userId = userSnapshot.getKey();
                        User user = userSnapshot.getValue(User.class);
                        UserDTO userDto = userToUserDTO(userId, user);
                        topUsers.add(userDto);
                    }
                }
                topUsers.sort((u1, u2) -> Long.compare(u2.getBit().getTotal_bit(), u1.getBit().getTotal_bit()));
                future.complete(topUsers);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
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
    public CompletableFuture<UserDTO> update(String userId, User user) {
        CompletableFuture<UserDTO> future = new CompletableFuture<>();

        DatabaseReference userRef = usersRef.child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // 사용자가 존재하는 경우에만 업데이트 수행
                    userRef.setValue(user, (databaseError, databaseReference) -> {
                        if (databaseError != null) {
                            future.completeExceptionally(databaseError.toException());
                        } else {
                            UserDTO userDTO = userToUserDTO(userId, user);
                            future.complete(userDTO);
                        }
                    });
                } else {
                    // 사용자가 존재하지 않는 경우, 예외 처리
                    future.completeExceptionally(new RuntimeException("User with ID " + userId + " does not exist."));
                }
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

        usersRef.child(id).removeValue((databaseError, databaseReference) -> {
            if (databaseError != null) {
                future.completeExceptionally(databaseError.toException());
            } else {
                future.complete(null);
            }
        });

        return future;
    }

    public UserDTO userToUserDTO(String userId,User user) {
        String username = user.getUsername();
        String userEmail = user.getEmail();
        String password = user.getPassword();
        Bit bit = user.getBit();
        Tree tree = user.getTree();
        return new UserDTO(userId, username, userEmail, password, bit, tree);
    }

}
