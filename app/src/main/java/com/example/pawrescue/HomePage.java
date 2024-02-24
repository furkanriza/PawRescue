package com.example.pawrescue;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pawrescue.Model.Animal;
import com.example.pawrescue.Model.User;
import com.example.pawrescue.UserContentProvider.UserContentProvider;
import com.example.pawrescue.UserContentProvider.UserContract;
import com.example.pawrescue.emergency.EmergencyHotlineActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class HomePage extends Fragment {

    private DatabaseReference databaseReference;
    private Context context;
    FirebaseDatabase database;
    private Uri selectedImageUri;

    public HomePage() {
        // Required empty public constructor
    }
    public HomePage(Context context) {
        this.context = context;
    }

    public static HomePage newInstance(User user) {
        HomePage fragment = new HomePage();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("animals");
    }
    private static final int PICK_IMAGE = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        //upload photo
        Button uploadPhotoButton = view.findViewById(R.id.btn_upload_photo);
        uploadPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        //save animal
        Button saveButton = view.findViewById(R.id.btn_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnimalToFirebase();
            }
        });
        return view;
    }

    //save animal to firebase//////////////////////////////////////////
    private void saveAnimalToFirebase() {
        // Get the values from EditTexts
        EditText petNameEditText = getView().findViewById(R.id.edit_pet_name);
        EditText situationEditText = getView().findViewById(R.id.edit_situation);

        String petName = petNameEditText.getText().toString();
        String situation = situationEditText.getText().toString();

        if (TextUtils.isEmpty(petName)) {
            makeToast("Please enter a pet name");
            return;
        }
        if (TextUtils.isEmpty(situation)) {
            makeToast("Please enter a situation");
            return;
        }
        if (selectedImageUri == null) {
            makeToast("Please select an image");
            return;
        }
        uploadImageAndSaveAnimal(petName, situation);
    }

    private void uploadImageAndSaveAnimal(String petName, String situation) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("animal_images");
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "_" + getFileName(selectedImageUri));

        fileRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String imageUrl = uri.toString();
                        saveAnimal(petName, situation, imageUrl);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                makeToast("Image upload failed: " + e.getMessage());
            }
        });
    }

    private void saveAnimal(String petName, String situation, String imageUrl) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            makeToast("User not authenticated");
            return;
        }
        Uri userUri = UserContract.UserEntry.CONTENT_URI;
        User user = UserContentProvider.fetchUser(userUri, getContext());
        String userNameAndSurname = user.getName() + " " + user.getSurname();

        Animal animal = new Animal(petName, situation, imageUrl, currentUser.getUid(), userNameAndSurname);

        databaseReference.push().setValue(animal, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    makeToast("Error saving animal: " + error.getMessage());
                } else {
                    makeToast("Animal saved successfully");
                }
            }
        });
    }


    /*private void saveAnimal(String petName, String situation, String imageUrl) {
        Uri userUri = UserContract.UserEntry.CONTENT_URI;
        User user = UserContentProvider.fetchUser(userUri, getContext());
        String nameAndSurname = user.getName() + " " + user.getSurname();
        Animal animal = new Animal(petName, situation, imageUrl, user.getId(), nameAndSurname);

        databaseReference.push().setValue(animal, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error != null) {
                    makeToast("Error saving animal: " + error.getMessage());
                } else {
                    makeToast("Animal saved successfully");
                }
            }
        });
    }*/

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (columnIndex != -1) {
                        result = cursor.getString(columnIndex);
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////////////

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // You might want to show the selected image on the UI
        }
    }

    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    //Added by Harun
        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Button btnEmergencyHotlines = view.findViewById(R.id.btnEmergencyHotlines);
            btnEmergencyHotlines.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Ensure getActivity() is not null
                    if (getActivity() != null) {
                        Intent intent = new Intent(getActivity(), EmergencyHotlineActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }


}