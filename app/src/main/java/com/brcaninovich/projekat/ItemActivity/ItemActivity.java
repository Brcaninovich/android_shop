package com.brcaninovich.projekat.ItemActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.brcaninovich.projekat.MessageActivity.MessageActivity;
import com.brcaninovich.projekat.R;
import com.brcaninovich.projekat.databinding.ActivityItemBinding;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.List;

public class ItemActivity extends AppCompatActivity {

    private ItemViewModel viewModel;
    private ActivityItemBinding binding;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        String artikalId = getIntent().getStringExtra("ARTIKAL_ID");

        viewModel.fetchArtikal(artikalId);

        viewModel.getArtikal().observe(this, new Observer<Artikal>() {
            @Override
            public void onChanged(Artikal artikal) {
                if (artikal != null) {
                    updateUI(artikal);
                    setupImageButtons(artikal);
                    checkIfUserCreatedArtikal(artikal);
                } else {
                    Toast.makeText(ItemActivity.this, "Artikal ne postoji", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.msngButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick();
            }
        });
    }

    private void handleButtonClick() {
        Artikal artikal = viewModel.getArtikal().getValue();
        if (artikal != null) {
            if (artikal.getDodaoKorisnik().equals(currentUser.getUid())) {
                deleteArtikal(artikal.getId());
            } else {
                sendMessage();
            }
        }
    }

    private void sendMessage() {
        Artikal artikal = viewModel.getArtikal().getValue();
        if (artikal != null && artikal.getDodaoKorisnik() != null) {
            String receiverId = artikal.getDodaoKorisnik();
            String currentUserId = currentUser.getUid();
            String chatId = viewModel.generateChatId(currentUserId, receiverId);

            Intent intent = new Intent(ItemActivity.this, MessageActivity.class);
            intent.putExtra("receiverId", receiverId);
            intent.putExtra("chatId", chatId);
            intent.putExtra("artikalId", artikal.getId());
            intent.putExtra("nazivArtikla", artikal.getNazivArtikla());
            startActivity(intent);
        } else {
            Toast.makeText(ItemActivity.this, "Nema podataka o prodavcu", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteArtikal(String artikalId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("artikli").document(artikalId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ItemActivity.this, "Artikal uspješno obrisan", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ItemActivity.this, "Greška pri brisanju artikla", Toast.LENGTH_SHORT).show();
                });
    }

    private void checkIfUserCreatedArtikal(Artikal artikal) {
        if (artikal.getDodaoKorisnik().equals(currentUser.getUid())) {
            binding.msngButton.setText("Obriši artikal");
        } else {
            binding.msngButton.setText("Pošalji poruku");
        }
    }

    private void updateUI(Artikal artikal) {
        binding.itemTopNameTextView.setText(artikal.getNazivArtikla());
        binding.itemPriceTextView.setText(artikal.getCijena());
        binding.itemDescTextView.setText(artikal.getOpisArtikla());
        binding.itemLocTextView.setText(artikal.getLokacija());
        updateItemImage(artikal);
    }

    private void setupImageButtons(Artikal artikal) {
        binding.backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentIndex = viewModel.getCurrentImageIndex();
                if (currentIndex > 0) {
                    viewModel.setCurrentImageIndex(currentIndex - 1);
                    updateItemImage(artikal);
                } else {
                    Toast.makeText(ItemActivity.this, "Nema prethodne slike", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.nextButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentIndex = viewModel.getCurrentImageIndex();
                List<String> slike = artikal.getSlike();
                if (currentIndex < slike.size() - 1) {
                    viewModel.setCurrentImageIndex(currentIndex + 1);
                    updateItemImage(artikal);
                } else {
                    Toast.makeText(ItemActivity.this, "Nema sledeće slike", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateItemImage(Artikal artikal) {
        List<String> slike = artikal.getSlike();
        int currentIndex = viewModel.getCurrentImageIndex();
        String currentImage = slike.get(currentIndex);

        Glide.with(this)
                .load(currentImage)
                .into(binding.itemImageView);
    }
}
