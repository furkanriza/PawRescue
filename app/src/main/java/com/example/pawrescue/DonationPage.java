package com.example.pawrescue;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.pawrescue.Model.Donation;
import com.example.pawrescue.Model.User;
import com.example.pawrescue.UserContentProvider.UserContentProvider;
import com.example.pawrescue.UserContentProvider.UserContract;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DonationPage extends Fragment {
    private ListView lvDonations;
    private List<Donation> donationList;
    private DonationAdapter adapter;
    // Firebase Database reference
    private DatabaseReference databaseReference;

    public DonationPage() {
        // Required empty public constructor
    }

    public static DonationPage newInstance() {
        return new DonationPage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Donations");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation_page, container, false);

        // Initialize UI elements
        EditText etDonationAmount = view.findViewById(R.id.etDonationAmount);
        Button btnDonate = view.findViewById(R.id.btnDonate);

        btnDonate.setOnClickListener(v -> {
            String donationAmount = etDonationAmount.getText().toString();
            if (!donationAmount.isEmpty()) {
                saveDonationToFirebase(donationAmount);
            } else {
                Toast.makeText(getActivity(), "Please enter a donation amount", Toast.LENGTH_SHORT).show();
            }
        });

        lvDonations = view.findViewById(R.id.lvDonations);
        donationList = new ArrayList<>();
        adapter = new DonationAdapter(getActivity(), donationList);
        lvDonations.setAdapter(adapter);

        fetchDonations();

        return view;
    }

    private void fetchDonations() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Donations");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                donationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Donation donation = snapshot.getValue(Donation.class);
                    donationList.add(donation);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void saveDonationToFirebase(String amount) {
        // Retrieve current user ID and user name
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Uri userUri = UserContract.UserEntry.CONTENT_URI;
        User user = UserContentProvider.fetchUser(userUri, getContext());
        String userNameAndSurname = user.getName() + " " + user.getSurname();

        // Format current date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Create a donation object
        Map<String, Object> donation = new HashMap<>();
        donation.put("userId", userId);
        donation.put("date", currentDate);
        donation.put("userNameAndSurname", userNameAndSurname);
        donation.put("amount", amount);

        // Save to Firebase
        databaseReference.push().setValue(donation)
                .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "Donation saved successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getActivity(), "Failed to save donation", Toast.LENGTH_SHORT).show());
    }
}
