package com.skku.BitCO2e.repository;

import com.google.firebase.database.*;
import com.skku.BitCO2e.DTO.AdvertisementDTO;
import com.skku.BitCO2e.model.Advertisement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AdvertisementRepository {
    private final DatabaseReference adsRef;

    public AdvertisementRepository(DatabaseReference adsRef) {
        this.adsRef = adsRef;
    }

    public CompletableFuture<AdvertisementDTO> addAdvertisement(Advertisement ad) {
        CompletableFuture<AdvertisementDTO> future = new CompletableFuture<>();
        String adId = adsRef.push().getKey();

        adsRef.child(adId).setValue(ad, (error, ref) -> {
            if (error != null) {
                future.completeExceptionally(error.toException());
            } else {
                AdvertisementDTO adDTO = convertToDTO(adId, ad);
                future.complete(adDTO);
            }
        });

        return future;
    }

    public CompletableFuture<AdvertisementDTO> findById(String adId) {
        CompletableFuture<AdvertisementDTO> future = new CompletableFuture<>();

        Query query = adsRef.child(adId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String adId = snapshot.getKey();
                    Advertisement ad = snapshot.getValue(Advertisement.class);
                    future.complete(convertToDTO(adId, ad));
                } else{
                    future.completeExceptionally(new RuntimeException("Advertisement not found"));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });
        return future;
    }

    public CompletableFuture<List<AdvertisementDTO>> findAllByUserId(String userId) {
        CompletableFuture<List<AdvertisementDTO>> future = new CompletableFuture<>();

        Query query = adsRef.orderByChild("userId").equalTo(userId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<AdvertisementDTO> ads = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    String adId = child.getKey();
                    Advertisement ad = child.getValue(Advertisement.class);
                    ads.add(convertToDTO(adId, ad));
                }
                future.complete(ads);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

    public CompletableFuture<List<AdvertisementDTO>> findAllByStatus(String status) {
        CompletableFuture<List<AdvertisementDTO>> future = new CompletableFuture<>();
        Query query = adsRef.orderByChild("status").equalTo(status);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<AdvertisementDTO> adDTOs = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Advertisement ad = snapshot.getValue(Advertisement.class);
                    if (ad != null) {
                        AdvertisementDTO adDTO = convertToDTO(snapshot.getKey(), ad);
                        adDTOs.add(adDTO);
                    }
                }
                future.complete(adDTOs);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

    public CompletableFuture<List<AdvertisementDTO>> findAllByStatusAndDate(String status, LocalDate date) {
        CompletableFuture<List<AdvertisementDTO>> future = new CompletableFuture<>();

        Query query = adsRef.orderByChild("status").equalTo(status);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<AdvertisementDTO> adDTOs = new ArrayList<>();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate formattedDate = LocalDate.parse(date.format(formatter), formatter);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Advertisement ad = snapshot.getValue(Advertisement.class);

                    if (ad != null) {
                        LocalDate adDate = LocalDate.parse(ad.getDate());
                        if(adDate.equals(formattedDate)){
                            AdvertisementDTO adDTO = convertToDTO(snapshot.getKey(), ad);
                            adDTOs.add(adDTO);
                        }
                    }
                }

                future.complete(adDTOs);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

    public CompletableFuture<Void> update(String adId, Advertisement ad) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        DatabaseReference adRef = adsRef.child(adId);

        adRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    adRef.setValue(ad, (error, ref) -> {
                        if (error != null) {
                            future.completeExceptionally(error.toException());
                        } else {
                            future.complete(null);
                        }
                    });
                } else {
                    future.completeExceptionally(new RuntimeException("Advertisement with Id" + adId + " not found"));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                future.completeExceptionally(error.toException());
            }
        });

        return future;
    }

//    public CompletableFuture<Void> deleteAll(){
//        CompletableFuture<Void> future = new CompletableFuture<>();
//
//        adsRef.removeValue(new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError error, DatabaseReference ref) {
//                if(error != null) {
//                    future.completeExceptionally(error.toException());
//                } else {
//                    future.complete(null);
//                }
//            }
//        });
//
//        return future;
//    }

    public AdvertisementDTO convertToDTO(String adID, Advertisement advertisement) {
        AdvertisementDTO advertisementDTO = new AdvertisementDTO(advertisement);
        advertisementDTO.setId(adID);
        return advertisementDTO;
    }
}
