package com.example.pawrescue.Authentication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pawrescue.Model.User;
import com.example.pawrescue.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends Fragment {

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText phoneNumberEditText;
    private EditText emailEditText;
    private EditText passwordEditText;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.login_background));

        nameEditText = view.findViewById(R.id.etName);
        surnameEditText = view.findViewById(R.id.etSurname);
        phoneNumberEditText = view.findViewById(R.id.etPhoneNumber);
        emailEditText = view.findViewById(R.id.register_email);
        passwordEditText = view.findViewById(R.id.register_password);

        Button registerButton = view.findViewById(R.id.register_button);
        mAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerWithFirebase();
            }
        });

        TextView navigateToLoginButton = view.findViewById(R.id.navigate_to_login_button);
        navigateToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToLoginFragment();
            }
        });

        return view;
    }

    public void registerWithFirebase(){
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser != null) {
                                String userId = firebaseUser.getUid();

                                String name = nameEditText.getText().toString().trim();
                                String surname = surnameEditText.getText().toString().trim();
                                String phoneNumber = phoneNumberEditText.getText().toString().trim();

                                User user = new User(name, surname, phoneNumber, email, "");

                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                databaseReference.child(userId).setValue(user)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    makeToast("Registration Successful");
                                                } else {
                                                    makeToast("Failed to save user details");
                                                }
                                            }
                                        });
                            }
                        } else {
                            makeToast("Registration Failed: " + task.getException().getMessage());
                        }
                    }
                });
    }

    private void navigateToLoginFragment() {
        NavHostFragment.findNavController(Register.this)
                .navigate(R.id.action_registerFragment_to_loginFragment);
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
