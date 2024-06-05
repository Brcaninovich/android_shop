package com.brcaninovich.projekat.MainApplication.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    private final MutableLiveData<List<Artikal>> artikliLiveData;
    private final FirebaseFirestore firestore;

    public DashboardViewModel() {
        artikliLiveData = new MutableLiveData<>();
        firestore = FirebaseFirestore.getInstance();
    }

    public LiveData<List<Artikal>> getArtikli() {
        return artikliLiveData;
    }

    public void loadArtikli() {
        firestore.collection("artikli")
                .orderBy("id", Query.Direction.ASCENDING)
                .limit(2)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Artikal> artikalList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Artikal artikal = document.toObject(Artikal.class);
                            artikalList.add(artikal);
                        }
                        artikliLiveData.setValue(artikalList);
                    } else {
                        artikliLiveData.setValue(null);
                    }
                });
    }

    public void searchArtikli(String query) {
        firestore.collection("artikli")
                .whereGreaterThanOrEqualTo("nazivArtikla", query)
                .whereLessThanOrEqualTo("nazivArtikla", query + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Artikal> artikalList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Artikal artikal = document.toObject(Artikal.class);
                            artikalList.add(artikal);
                        }
                        artikliLiveData.setValue(artikalList);
                    } else {
                        artikliLiveData.setValue(null);
                    }
                });
    }

    public void filterByKategorija(String kategorija) {
        firestore.collection("artikli")
                .whereEqualTo("kategorija", kategorija)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Artikal> artikalList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Artikal artikal = document.toObject(Artikal.class);
                            artikalList.add(artikal);
                        }
                        artikliLiveData.setValue(artikalList);
                    } else {
                        artikliLiveData.setValue(null);
                    }
                });
    }
}
