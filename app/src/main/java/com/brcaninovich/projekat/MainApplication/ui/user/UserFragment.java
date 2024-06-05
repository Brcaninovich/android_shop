package com.brcaninovich.projekat.MainApplication.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.brcaninovich.projekat.LoginRegister.activity.LoginActivity;
import com.brcaninovich.projekat.MainActivity;
import com.brcaninovich.projekat.databinding.FragmentUserBinding;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFragment extends Fragment {

    private FragmentUserBinding binding;
    private FirebaseAuth mAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        UserViewModel userViewModel =
                new ViewModelProvider(this).get(UserViewModel.class);

        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        final TextView textView = binding.textUserData;
        final MaterialButton changePasswordButton = binding.buttonChangePassword;
        final MaterialButton logoutButton = binding.buttonLogout;
        final MaterialButton viewItemsButton = binding.buttonViewItems;

        if (currentUser != null) {
            textView.setText(currentUser.getEmail());
        }

        changePasswordButton.setOnClickListener(view -> {
            if (currentUser != null) {
                String userEmail = currentUser.getEmail();
                if (userEmail != null && !userEmail.isEmpty()) {
                    mAuth.sendPasswordResetEmail(userEmail)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getActivity(), "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "Failed to send reset email.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "User email not found.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "User not logged in.", Toast.LENGTH_SHORT).show();
            }
        });

        logoutButton.setOnClickListener(view -> {
            mAuth.signOut();
            Toast.makeText(getActivity(), "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        viewItemsButton.setOnClickListener(view -> {
            //TODO: dodat intent za otvaranje ItemsActivity koji prikazuje userove iteme
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
