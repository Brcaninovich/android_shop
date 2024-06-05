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

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    private FirebaseAuth mAuth;

    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
    }

    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            loginResult.setValue(true);
                        } else {
                            Log.w("LoginViewModel", "signInWithEmail:failure", task.getException());
                            loginResult.setValue(false);
                        }
                    }
                });
    }
}
