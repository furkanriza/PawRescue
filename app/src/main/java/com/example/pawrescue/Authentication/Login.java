package com.example.pawrescue.Authentication;

import static android.content.ContentValues.TAG;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.pawrescue.Activity2;
import com.example.pawrescue.Model.User;
import com.example.pawrescue.R;
import com.example.pawrescue.UserContentProvider.DatabaseHelper;
import com.example.pawrescue.UserContentProvider.UserContentProvider;
import com.example.pawrescue.UserContentProvider.UserContract;
import com.example.pawrescue.database_unused.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mAuth;
    DatabaseHelper databaseHelper;
    private UserRepository userRepository;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        view.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.login_background));
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        databaseHelper = new DatabaseHelper(getActivity());

        emailEditText = view.findViewById(R.id.etEmail);
        passwordEditText = view.findViewById(R.id.etPassword);

        if (sharedPreferences.getBoolean("rememberMe", true)) {
            Uri userUri = UserContract.UserEntry.CONTENT_URI;
            User user = UserContentProvider.fetchUser(userUri, getContext());
            emailEditText.setText(user.getEmail());
            passwordEditText.setText(user.getPassword());
        }


        mAuth = FirebaseAuth.getInstance();

        TextView navigateToRegister = view.findViewById(R.id.navigate_to_register_button);
        navigateToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(Login.this)
                        .navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        Button btnLogin = view.findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserWithFireBase();
                //loginWithSQLite();
            }
        });

        return view;
    }

    private void loginUserWithFireBase() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        CheckBox rememberMeCheckBox = getActivity().findViewById(R.id.checkboxRememberMe);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        if (firebaseUser != null) {
                            // Fetch other user data from Firebase Realtime Database
                            fetchUserDataFromFirebase(firebaseUser.getUid(), user -> {
                                // Set the user ID and save to SQLite
                                user.setId(firebaseUser.getUid());

                                // Save "Remember Me" preference to SharedPreferences
                                saveRememberMePreference(rememberMeCheckBox.isChecked());

                                if (rememberMeCheckBox.isChecked()) {
                                    saveUserToSQLite(user, password);
                                }

                                // Proceed to next activity
                                Intent intent = new Intent(getActivity(), Activity2.class);
                                startActivity(intent);
                            });
                        }
                    } else {
                        Log.d(TAG, "createUserWithEmail:failure", task.getException());
                        makeToast("Login Failed: " + task.getException().getMessage());
                    }
                });
    }


    private void saveUserToSQLite(User user, String password) {
        databaseHelper.onUpgrade(databaseHelper.getWritableDatabase(), 1, 1);
        String userId = user.getId();
        ContentValues values = new ContentValues();
        values.put(UserContract.UserEntry.COLUMN_USER_ID, userId);
        values.put(UserContract.UserEntry.COLUMN_EMAIL, user.getEmail());
        values.put(UserContract.UserEntry.COLUMN_PASSWORD, password);
        values.put(UserContract.UserEntry.COLUMN_NAME, user.getName());
        values.put(UserContract.UserEntry.COLUMN_SURNAME, user.getSurname());
        values.put(UserContract.UserEntry.COLUMN_PHONE_NUMBER, user.getPhoneNumber());

        Uri insertedUri = getActivity().getContentResolver().insert(UserContract.UserEntry.CONTENT_URI, values);

        if (insertedUri != null) {
            makeToast("User added to SQLite database");
        } else {
            makeToast("Failed to add user to SQLite database");
        }
    }

    private void saveRememberMePreference(boolean isChecked) {
        editor.putBoolean("rememberMe", isChecked);
        editor.apply();
    }


    private void fetchUserDataFromFirebase(String userId, OnUserDataFetchedListener listener) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    listener.onUserDataFetched(user);
                } else {
                    Log.d(TAG, "User data not found");
                    // Handle the case where user data is not found
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Database error: " + databaseError.getMessage());
                // Handle the database error
            }
        });
    }

    interface OnUserDataFetchedListener {
        void onUserDataFetched(User user);
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
