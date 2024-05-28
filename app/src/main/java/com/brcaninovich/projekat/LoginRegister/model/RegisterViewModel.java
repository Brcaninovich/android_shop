package com.brcaninovich.projekat.LoginRegister.model;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RegisterViewModel extends ViewModel {
    private MutableLiveData<Boolean> registerResult = new MutableLiveData<>();
    private FirebaseAuth mAuth;

    public RegisterViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<Boolean> getRegisterResult() {
        return registerResult;
    }

    public void register(String email, String password, String passwordAgain) {
        if (!password.equals(passwordAgain)) {
            registerResult.setValue(false);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            registerResult.setValue(true);
                        } else {
                            Log.w("RegisterViewModel", "createUserWithEmail:failure", task.getException());
                            registerResult.setValue(false);
                        }
                    }
                });
    }
}
