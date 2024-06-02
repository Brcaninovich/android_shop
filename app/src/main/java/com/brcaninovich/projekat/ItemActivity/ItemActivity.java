package com.brcaninovich.projekat.ItemActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.brcaninovich.projekat.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ItemActivity extends AppCompatActivity {

    private ItemViewModel viewModel;
    private FirebaseFirestore db;
    private ImageView itemImageView;
    private ImageView backButtonImageView;
    private ImageView nextButtonImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        db = FirebaseFirestore.getInstance();

        itemImageView = findViewById(R.id.itemImageView);
        backButtonImageView = findViewById(R.id.backButtonImageView);
        nextButtonImageView = findViewById(R.id.nextButtonImageView);

        String artikalId = getIntent().getStringExtra("ARTIKAL_ID");

        db.collection("artikli").document(artikalId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Artikal artikal = documentSnapshot.toObject(Artikal.class);
                        if (artikal != null) {
                            viewModel.setArtikal(artikal);
                        } else {
                            Toast.makeText(ItemActivity.this, "Artikal ne postoji", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ItemActivity.this, "Greška pri dohvatu artikla", Toast.LENGTH_SHORT).show();
                    }
                });

        viewModel.getArtikal().observe(this, new Observer<Artikal>() {
            @Override
            public void onChanged(Artikal artikal) {
                updateUI(artikal);
                setupImageButtons(artikal);
            }
        });
    }

    private void updateUI(Artikal artikal) {
        TextView itemNameTextView = findViewById(R.id.itemTopNameTextView);
        TextView itemPriceTextView = findViewById(R.id.itemPriceTextView);
        TextView itemDescTextView = findViewById(R.id.itemDescTextView);
        TextView itemLocTextView = findViewById(R.id.itemLocTextView);

        itemNameTextView.setText(artikal.getNazivArtikla());
        itemPriceTextView.setText(artikal.getCijena());
        itemDescTextView.setText(artikal.getOpisArtikla());
        itemLocTextView.setText(artikal.getLokacija());
    }

    private void setupImageButtons(Artikal artikal) {
        backButtonImageView.setOnClickListener(new View.OnClickListener() {
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

        nextButtonImageView.setOnClickListener(new View.OnClickListener() {
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

        updateItemImage(artikal);
    }

    private void updateItemImage(Artikal artikal) {
        List<String> slike = artikal.getSlike();
        int currentIndex = viewModel.getCurrentImageIndex();
        String currentImage = slike.get(currentIndex);

        Glide.with(this)
                .load(currentImage)
                .into(itemImageView);
    }
}
