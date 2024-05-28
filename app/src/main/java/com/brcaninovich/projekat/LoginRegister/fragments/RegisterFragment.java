package com.brcaninovich.projekat.LoginRegister.fragments;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.brcaninovich.projekat.LoginRegister.model.LoginViewModel;
import com.brcaninovich.projekat.LoginRegister.model.RegisterViewModel;
import com.brcaninovich.projekat.R;
import com.google.firebase.FirebaseApp;

public class RegisterFragment extends Fragment {

    private RegisterViewModel registerViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        bindComponents(view);



        registerViewModel.getRegisterResult().observe(getViewLifecycleOwner(), registerResult -> {
            if (registerResult) {
                Toast.makeText(getActivity(), "Register Successful", Toast.LENGTH_SHORT).show();
                changeToLoginFragment();
            } else {
                Toast.makeText(getActivity(), "Register Failed", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void bindComponents(View view) {
        EditText emailEditText = view.findViewById(R.id.register_email);
        EditText passwordEditText = view.findViewById(R.id.register_password);
        EditText passwordAgainEditText = view.findViewById(R.id.register_password_email);
        Button registerButton = view.findViewById(R.id.register_button);
        TextView registerLink = view.findViewById(R.id.register_link);

        registerButton.setOnClickListener(v -> {
            String username = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String passwordAgain = passwordAgainEditText.getText().toString();
            registerViewModel.register(username, password, passwordAgain);
        });

        registerLink.setOnClickListener(v -> {
            changeToLoginFragment();
        });
    }

    private void changeToLoginFragment() {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
