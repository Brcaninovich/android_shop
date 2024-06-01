package com.brcaninovich.projekat.MainApplication.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.brcaninovich.projekat.databinding.FragmentNotificationsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        MaterialSpinner spinner = (MaterialSpinner) binding.spinner;
        spinner.setItems("Odaberi kategoriju", "Automobili", "Odjeća", "Računari", "Mobiteli", "Sport", "Nakit");

        MaterialSpinner spinner2 = (MaterialSpinner) binding.spinner2;
        spinner2.setItems("Odaberi Stanje", "Novo", "Korišteno");

        MaterialSpinner spinner3 = (MaterialSpinner) binding.spinner3;
        spinner3.setItems("Odaberi Lokaciju", "Tuzla", "Živinice", "Kalesija", "Banovići");

        // Postavite onClick listener za spremanje podataka
        binding.saveButton.setOnClickListener(v -> {
            String nazivArtikla = binding.addNazivArtikla.getText().toString();
            String opisArtikla = binding.addOpisArtikla.getText().toString();
            String cijenaArtikla = binding.addCijenaArtikla.getText().toString();
            String kategorija = spinner.getText().toString();
            String stanje = spinner2.getText().toString();
            String lokacija = spinner3.getText().toString();

            List<String> slike = new ArrayList<>();

            Artikal artikal = new Artikal(null, nazivArtikla, slike, opisArtikla, cijenaArtikla, kategorija, stanje, lokacija);

            notificationsViewModel.saveArtikal(artikal);
            Snackbar.make(v, "Podaci su uspješno pohranjeni!", Snackbar.LENGTH_LONG).show();
        });

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show();
            }
        });
        /*final TextView textView = binding.textNotifications;*/
        /*notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);*/
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}