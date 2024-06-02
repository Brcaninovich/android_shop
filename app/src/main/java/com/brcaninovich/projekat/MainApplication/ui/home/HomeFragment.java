package com.brcaninovich.projekat.MainApplication.ui.home;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brcaninovich.projekat.ItemActivity.ItemActivity;
import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.brcaninovich.projekat.MainApplication.ui.home.adapter.AdapterRecyclerView;
import com.brcaninovich.projekat.MainApplication.ui.home.adapter.OnItemClickListener;
import com.brcaninovich.projekat.R;
import com.brcaninovich.projekat.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnItemClickListener {

    private FragmentHomeBinding binding;
    private AdapterRecyclerView adapter;
    private List<Artikal> artikliList;
    private FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.homeRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdapterRecyclerView();
        recyclerView.setAdapter(adapter);

        artikliList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        // Dohvat podataka iz Firestore-a
        db.collection("artikli")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Artikal artikal = documentSnapshot.toObject(Artikal.class);
                            artikliList.add(artikal);
                        }
                        adapter.setData(artikliList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Greška pri dohvatu podataka", Toast.LENGTH_SHORT).show();
                    }
                });

        adapter.setOnItemClickListener(this); // Postavljanje listenera na adapter

        return root;
    }

    @Override
    public void onItemClick(Artikal artikal) {
        // Ovde definisati šta se dešava kada korisnik klikne na stavku
        // Na primer, otvori novu aktivnost za prikaz detalja o artiklu
        Intent intent = new Intent(getContext(), ItemActivity.class);
        intent.putExtra("ARTIKAL_ID", artikal.getId()); // Prosledi ID artikla na sledeću aktivnost
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}