package com.brcaninovich.projekat.LoginRegister.model;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Boolean> loginResult = new MutableLiveData<>();

    public LiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        if (username.equals("user") && password.equals("password")) {
            loginResult.setValue(true);
        } else {
            loginResult.setValue(false);
        }
    }
}

