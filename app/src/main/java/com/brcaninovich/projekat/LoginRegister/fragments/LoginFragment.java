package com.brcaninovich.projekat.LoginRegister.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.brcaninovich.projekat.LoginRegister.model.LoginViewModel;
import com.brcaninovich.projekat.MainApplication.StartActivity;
import com.brcaninovich.projekat.R;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_REMEMBER_ME = "remember_me";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        bindComponents(view);

        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), loginResult -> {
            if (loginResult) {
                Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                CheckBox rememberMeCheckBox = view.findViewById(R.id.login_rememberMe);
                saveRememberMeOption(rememberMeCheckBox.isChecked());
                Intent intent = new Intent(getActivity(), StartActivity.class);
                startActivity(intent);
                getActivity().finish();
            } else {
                Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void bindComponents(View view) {
        EditText usernameEditText = view.findViewById(R.id.login_email);
        EditText passwordEditText = view.findViewById(R.id.login_password);
        Button loginButton = view.findViewById(R.id.login_button);
        TextView registerLink = view.findViewById(R.id.login_link);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            loginViewModel.login(username, password);
        });

        registerLink.setOnClickListener(v -> {
            changeToRegisterFragment();
        });
    }

    private void saveRememberMeOption(boolean rememberMe) {
        SharedPreferences prefs = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_REMEMBER_ME, rememberMe);
        editor.apply();
    }

    private void changeToRegisterFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new RegisterFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
