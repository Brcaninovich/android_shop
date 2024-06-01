package com.brcaninovich.projekat.MainApplication.ui.notifications;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.brcaninovich.projekat.MainApplication.Objects.Artikal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final FirebaseFirestore firestore;

    public NotificationsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
        firestore = FirebaseFirestore.getInstance();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void saveArtikal(Artikal artikal) {
        firestore.collection("artikli")
                .add(artikal)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        artikal.setId(id);
                        firestore.collection("artikli").document(id)
                                .set(artikal)
                                .addOnSuccessListener(aVoid -> {
                                    Log.d(TAG, "DocumentSnapshot successfully updated with ID");
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "Error updating document with ID: " + e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}