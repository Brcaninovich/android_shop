package com.brcaninovich.projekat.MainApplication.ui.dashboard;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.brcaninovich.projekat.MainApplication.ui.dashboard.adapter.ArtikalAdapter;
import com.brcaninovich.projekat.databinding.FragmentDashboardBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    private ArtikalAdapter artikalAdapter;
    private FirebaseFirestore firestore;
    private List<Artikal> artikalList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicijalizacija Firestore-a
        firestore = FirebaseFirestore.getInstance();
        artikalList = new ArrayList<>();
        artikalAdapter = new ArtikalAdapter(getContext(), artikalList);

        // Postavljanje RecyclerView-a
        RecyclerView recyclerView = binding.recyclerViewSearch;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(artikalAdapter);

        // Dohvaćanje podataka iz Firestore-a
        loadArtikli();

        // Pretraga po ukucanom tekstu
        binding.pretragaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString();
                if (query.isEmpty()) {
                    loadArtikli();
                } else {
                    searchArtikli(query);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Klik na kategoriju
        binding.category1ImageView.setOnClickListener(view -> filterByKategorija("Automobili"));
        binding.category2ImageView.setOnClickListener(view -> filterByKategorija("Mobiteli"));
        // Dodajte slične klik eventove za ostale kategorije

        return root;
    }

    private void loadArtikli() {
        firestore.collection("artikli")
                .orderBy("id", Query.Direction.DESCENDING)  // Koristite polje koje imate
                .limit(2)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        artikalList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Artikal artikal = document.toObject(Artikal.class);
                            artikalList.add(artikal);
                        }
                        artikalAdapter.setArtikli(artikalList);
                    } else {
                        // Handle error
                    }
                });
    }

    private void searchArtikli(String query) {
        firestore.collection("artikli")
                .whereGreaterThanOrEqualTo("nazivArtikla", query)
                .whereLessThanOrEqualTo("nazivArtikla", query + "\uf8ff")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        artikalList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Artikal artikal = document.toObject(Artikal.class);
                            artikalList.add(artikal);
                        }
                        artikalAdapter.setArtikli(artikalList);
                    } else {
                        // Handle error
                    }
                });
    }

    private void filterByKategorija(String kategorija) {
        firestore.collection("artikli")
                .whereEqualTo("kategorija", kategorija)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        artikalList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Artikal artikal = document.toObject(Artikal.class);
                            artikalList.add(artikal);
                        }
                        artikalAdapter.setArtikli(artikalList);
                    } else {
                        // Handle error
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
