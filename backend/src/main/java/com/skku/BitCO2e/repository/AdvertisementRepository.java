package com.skku.BitCO2e.repository;

import com.google.firebase.database.*;
import com.skku.BitCO2e.DTO.AdvertisementDTO;
import com.skku.BitCO2e.model.Advertisement;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class AdvertisementRepository {
    private DatabaseReference adsRef;

    public AdvertisementRepository() {
        this.adsRef = FirebaseDatabase.getInstance().getReference("advertisements");
    }

    public AdvertisementRepository(DatabaseReference adsRef) {
        this.adsRef = adsRef;
    }

    public CompletableFuture<AdvertisementDTO> addAdvertisement(Advertisement ad) {
        CompletableFuture<AdvertisementDTO> future = new CompletableFuture<>();
        String adId = adsRef.push().getKey();

        adsRef.child(adId).setValue(ad, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error != null) {
                    future.completeExceptionally(error.toException());
                } else {
                    AdvertisementDTO adDTO = convertToDTO(adId, ad);
                    future.complete(adDTO);
                }
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

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate currentDate = LocalDate.now(ZoneId.systemDefault());
                LocalDate formattedDate = LocalDate.parse(currentDate.format(formatter));

                if (status.equals("approved")) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Advertisement advertisement = snapshot.getValue(Advertisement.class);
                        if (advertisement != null) {
                            LocalDate adDate = LocalDate.parse(advertisement.getDate());
                            if (adDate.equals(formattedDate.minusDays(1))) {
                                AdvertisementDTO adDTO = convertToDTO(snapshot.getKey(), advertisement);
                                adDTOs.add(adDTO);
                            }
                        }
                    }
                } else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Advertisement advertisement = snapshot.getValue(Advertisement.class);
                        if (advertisement != null) {
                            AdvertisementDTO adDTO = convertToDTO(snapshot.getKey(), advertisement);
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
