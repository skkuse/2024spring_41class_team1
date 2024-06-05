package com.skku.BitCO2e.service;

import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

@Service
public class BitService {

    public void addBits(String userId, long bitsToAdd) {
        DatabaseReference bitRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bit");

        bitRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Long currentBit = dataSnapshot.child("current_bit").getValue(Long.class);
                    Long totalBit = dataSnapshot.child("total_bit").getValue(Long.class);

                    if (currentBit != null && totalBit != null) {
                        currentBit += bitsToAdd;
                        totalBit += bitsToAdd;

                        bitRef.child("current_bit").setValueAsync(currentBit);
                        bitRef.child("total_bit").setValueAsync(totalBit);

                        System.out.println("Updated Current Bit: " + currentBit);
                        System.out.println("Updated Total Bit: " + totalBit);
                    } else {
                        System.out.println("Either current_bit or total_bit does not exist.");
                    }
                } else {
                    System.out.println("bit data does not exist.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error: " + databaseError.getMessage());
            }
        });
    }

    public void subtractBits(String adId) {
        DatabaseReference adRef = FirebaseDatabase.getInstance().getReference("advertisements").child(adId);

        adRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot adSnapshot) {
                if (adSnapshot.exists()) {
                    String userId = adSnapshot.child("userId").getValue(String.class);
                    String usedBitStr = adSnapshot.child("usedBit").getValue(String.class);
                    if (userId != null && usedBitStr != null) {
                        try {
                            Long usedBit = Long.parseLong(usedBitStr);
                            DatabaseReference currentBitRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("bit").child("current_bit");

                            currentBitRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Long currentBit = dataSnapshot.getValue(Long.class);
                                        if (currentBit != null) {
                                            if (currentBit >= usedBit) {
                                                currentBit -= usedBit;
                                                currentBitRef.setValueAsync(currentBit);
                                                System.out.println("Updated Current Bit: " + currentBit);
                                            } else {
                                                System.out.println("Not enough bits to subtract.");
                                            }
                                        }
                                    } else {
                                        System.out.println("current_bit does not exist.");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    System.err.println("Error: " + databaseError.getMessage());
                                }
                            });
                        } catch (NumberFormatException e) {
                            System.err.println("Error: usedBit is not a valid number.");
                        }
                    } else {
                        System.out.println("userId or usedBit is missing in the advertisement.");
                    }
                } else {
                    System.out.println("Advertisement does not exist.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error: " + databaseError.getMessage());
            }
        });
    }

}
