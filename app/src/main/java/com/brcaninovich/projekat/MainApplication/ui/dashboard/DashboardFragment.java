package com.brcaninovich.projekat.MainApplication.ui.dashboard;

import android.content.Intent;
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

import com.brcaninovich.projekat.ItemActivity.ItemActivity;
import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.brcaninovich.projekat.MainApplication.ui.dashboard.adapter.ArtikalAdapter;
import com.brcaninovich.projekat.databinding.FragmentDashboardBinding;


public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel dashboardViewModel;
    private ArtikalAdapter artikalAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);

        initRecyclerView();
        observeViewModel();
        setupSearch();
        setupCategoryClicks();


        dashboardViewModel.loadArtikli();

        return binding.getRoot();
    }

    private void initRecyclerView() {
        artikalAdapter = new ArtikalAdapter(getContext(), null);
        artikalAdapter.setOnItemClickListener(new ArtikalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Artikal artikal) {

                Intent intent = new Intent(getContext(), ItemActivity.class);
                intent.putExtra("ARTIKAL_ID", artikal.getId());
                startActivity(intent);
            }
        });

        binding.recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewSearch.setAdapter(artikalAdapter);
    }

    private void observeViewModel() {
        dashboardViewModel.getArtikli().observe(getViewLifecycleOwner(), artikli -> {
            if (artikli != null) {
                artikalAdapter.setArtikli(artikli);
            } else {

            }
        });
    }

    private void setupSearch() {
        binding.pretragaEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString();
                if (query.isEmpty()) {
                    dashboardViewModel.loadArtikli();
                } else {
                    dashboardViewModel.searchArtikli(query);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void setupCategoryClicks() {
        binding.category1ImageView.setOnClickListener(view -> dashboardViewModel.filterByKategorija("Automobili"));
        binding.category2ImageView.setOnClickListener(view -> dashboardViewModel.filterByKategorija("Mobiteli"));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
