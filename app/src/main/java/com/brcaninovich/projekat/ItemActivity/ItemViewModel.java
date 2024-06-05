package com.brcaninovich.projekat.ItemActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemViewModel extends ViewModel {

    private MutableLiveData<Artikal> artikalLiveData = new MutableLiveData<>();
    private int currentImageIndex = 0;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void fetchArtikal(String artikalId) {
        db.collection("artikli").document(artikalId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Artikal artikal = documentSnapshot.toObject(Artikal.class);
                        artikalLiveData.setValue(artikal);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        artikalLiveData.setValue(null);
                    }
                });
    }

    public void setArtikal(Artikal artikal) {
        artikalLiveData.setValue(artikal);
        setCurrentImageIndex(0);
    }

    public LiveData<Artikal> getArtikal() {
        return artikalLiveData;
    }

    public int getCurrentImageIndex() {
        return currentImageIndex;
    }

    public void setCurrentImageIndex(int index) {
        currentImageIndex = index;
    }

    public String generateChatId(String userId1, String userId2) {
        List<String> ids = Arrays.asList(userId1, userId2);
        Collections.sort(ids);
        return ids.get(0) + "_" + ids.get(1);
    }
}