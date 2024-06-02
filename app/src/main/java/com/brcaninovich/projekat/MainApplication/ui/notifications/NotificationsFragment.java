package com.brcaninovich.projekat.MainApplication.ui.notifications;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.brcaninovich.projekat.databinding.FragmentNotificationsBinding;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationsViewModel notificationsViewModel;
    private Uri[] imageUris = new Uri[3];
    private int currentImageIndex = 0;
    private int uploadedImagesCount = 0;
    private List<String> imageUrls = new ArrayList<>();
    private FirebaseStorage storage;
    private ImageView[] imageViews;
    private ImageView[] placeholderImageViews;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        storage = FirebaseStorage.getInstance();

        // Initialize spinners
        MaterialSpinner spinner = binding.spinner;
        spinner.setItems("Odaberi kategoriju", "Automobili", "Odjeća", "Računari", "Mobiteli", "Sport", "Nakit");

        MaterialSpinner spinner2 = binding.spinner2;
        spinner2.setItems("Odaberi Stanje", "Novo", "Korišteno");

        MaterialSpinner spinner3 = binding.spinner3;
        spinner3.setItems("Odaberi Lokaciju", "Tuzla", "Živinice", "Kalesija", "Banovići");

        // Initialize image views array
        imageViews = new ImageView[]{binding.addNewImageButton, binding.addNewImageButton2, binding.addNewImageButton3};
        placeholderImageViews = new ImageView[]{binding.photoHolderImageView, binding.photoHolderImageView2, binding.photoHolderImageView3};

        // Add logic for image selection
        binding.cardViewAddPhoto.setOnClickListener(v -> {
            currentImageIndex = 0;
            selectImage();
        });

        binding.cardViewAddPhoto2.setOnClickListener(v -> {
            currentImageIndex = 1;
            selectImage();
        });

        binding.cardViewAddPhoto3.setOnClickListener(v -> {
            currentImageIndex = 2;
            selectImage();
        });

        // Set onClick listener to save data
        binding.saveButton.setOnClickListener(v -> {
            saveArtikal();
        });

        return root;
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                        placeholderImageViews[currentImageIndex].setVisibility(View.GONE);
                        Glide.with(this)
                                .load(selectedImageUri)
                                .centerCrop()
                                .into(imageViews[currentImageIndex]);

                        // Store the URI in the array
                        imageUris[currentImageIndex] = selectedImageUri;

                        // Make the second card view visible if an image was selected for the first one
                        if (currentImageIndex == 0) {
                            binding.cardViewHolderPhoto2.setVisibility(View.VISIBLE);
                        } else if (currentImageIndex == 1) {
                            binding.cardViewHolderPhoto3.setVisibility(View.VISIBLE);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
    );

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        imagePickerLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void saveArtikal() {
        uploadedImagesCount = 0;
        imageUrls.clear();

        boolean hasImagesToUpload = false;
        for (Uri uri : imageUris) {
            if (uri != null) {
                hasImagesToUpload = true;
                uploadImageToStorage(uri);
            }
        }

        if (!hasImagesToUpload) {
            saveArtikalToFirestore();
        }
    }

    private void uploadImageToStorage(Uri uri) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "images/" + timestamp + "_" + uri.getLastPathSegment();
        StorageReference storageRef = storage.getReference().child(fileName);

        storageRef.putFile(uri)
                .addOnSuccessListener(taskSnapshot -> storageRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    imageUrls.add(uri1.toString());
                    uploadedImagesCount++;
                    if (uploadedImagesCount == getNonNullUriCount()) {
                        saveArtikalToFirestore();
                    }
                }))
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                });
    }

    private int getNonNullUriCount() {
        int count = 0;
        for (Uri uri : imageUris) {
            if (uri != null) {
                count++;
            }
        }
        return count;
    }

    private void saveArtikalToFirestore() {
        String nazivArtikla = binding.addNazivArtikla.getText().toString();
        String opisArtikla = binding.addOpisArtikla.getText().toString();
        String cijenaArtikla = binding.addCijenaArtikla.getText().toString();
        String kategorija = binding.spinner.getText().toString();
        String stanje = binding.spinner2.getText().toString();
        String lokacija = binding.spinner3.getText().toString();
        String dodaoKorisnik = FirebaseAuth.getInstance().getCurrentUser().getUid();

        List<String> slike = new ArrayList<>(imageUrls); // Add images

        Artikal artikal = new Artikal(null, nazivArtikla, slike, opisArtikla, cijenaArtikla, kategorija, stanje, lokacija, dodaoKorisnik);

        notificationsViewModel.saveArtikal(artikal);
        Snackbar.make(getView(), "Podaci su uspješno pohranjeni!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
