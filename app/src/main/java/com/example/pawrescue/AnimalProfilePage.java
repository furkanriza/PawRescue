package com.example.pawrescue;

import static android.content.ContentValues.TAG;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.pawrescue.Model.Animal;
import com.example.pawrescue.Model.User;
import com.example.pawrescue.UserContentProvider.UserContentProvider;
import com.example.pawrescue.UserContentProvider.UserContract;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnimalProfilePage extends Fragment {
    private Button btnDelete;
    private Button btnUpdate;
    private Animal currentAnimal;
    private static final String ARG_ANIMAL_ID = "animal_id";
    private static final String ARG_ANIMAL_NAME = "animal_name";
    private static final String ARG_ANIMAL_DESCRIPTION = "animal_description";
    private static final String ARG_ANIMAL_IMAGE_URL = "animal_image_url";
    private static final String ARG_ANIMAL_POSTER_ID = "animal_poster_id";
    private static final String ARG_USER_NAME_AND_SURNAME = "user_name";

    private String animalId;
    private String animalName;
    private String animalDescription;
    private String animalImageUrl;
    private String animalPosterId;
    private String userNameAndSurname;
    Button deleteButton;
    Button updateButton;

    public AnimalProfilePage() {
        // Required empty public constructor
    }

    public static AnimalProfilePage newInstance(Animal animal) {
        AnimalProfilePage fragment = new AnimalProfilePage();
        Bundle args = new Bundle();
        args.putString(ARG_ANIMAL_NAME, animal.getName());
        args.putString(ARG_ANIMAL_DESCRIPTION, animal.getDescription());
        args.putString(ARG_ANIMAL_IMAGE_URL, animal.getImageUrl());
        args.putString(ARG_ANIMAL_POSTER_ID, animal.getPosterId());
        args.putString(ARG_ANIMAL_ID, animal.getId());
        args.putString(ARG_USER_NAME_AND_SURNAME, animal.getPostedByNameAndSurname());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            animalId = getArguments().getString(ARG_ANIMAL_ID);
            animalName = getArguments().getString(ARG_ANIMAL_NAME);
            animalDescription = getArguments().getString(ARG_ANIMAL_DESCRIPTION);
            animalImageUrl = getArguments().getString(ARG_ANIMAL_IMAGE_URL);
            animalPosterId = getArguments().getString(ARG_ANIMAL_POSTER_ID);
            makeToast(animalPosterId);
            userNameAndSurname = getArguments().getString(ARG_USER_NAME_AND_SURNAME);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal_profile_page, container, false);
        TextView nameTextView = view.findViewById(R.id.tv_name);
        TextView descriptionTextView = view.findViewById(R.id.tv_description);
        ImageView animalImageView = view.findViewById(R.id.iv_animal);
        TextView postedByTextView = view.findViewById(R.id.tv_posted_by);

        nameTextView.setText(animalName);
        descriptionTextView.setText(animalDescription);
        postedByTextView.setText(userNameAndSurname);

        if (animalImageUrl != null && !animalImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(animalImageUrl)
                    .into(animalImageView); // Load the image
        }

        updateButton = view.findViewById(R.id.btn_update);
        deleteButton = view.findViewById(R.id.btn_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAnimal(animalId);
            }
        });

        setupButtonVisibility();

        return view;
    }
    
    private void setupButtonVisibility() {
        Uri userUri = UserContract.UserEntry.CONTENT_URI;
        User currentUser = UserContentProvider.fetchUser(userUri, getActivity());
        Log.d(TAG, "Current User Id: " + currentUser.getId() + "\n\n\n\nAnimal Poster Id: " + animalPosterId);
        if (currentUser.getId().equals(animalPosterId)) {
            deleteButton.setVisibility(View.VISIBLE);
            updateButton.setVisibility(View.VISIBLE);
        } else {
            deleteButton.setVisibility(View.GONE);
            updateButton.setVisibility(View.GONE);
        }
    }
    

    private void deleteAnimal(String animalId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("animals");
        databaseReference.child(animalId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getActivity(), "Animal deleted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Failed to delete animal", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}

