package com.example.pawrescue;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.pawrescue.UserContentProvider.UserContract;

public class UserDataDisplay extends Fragment {

    TextView tvUserName;
    TextView tvUserSurname;
    TextView tvUserEmail;
    TextView tvUserPhoneNumber;
    public UserDataDisplay() {
    }

    public static UserDataDisplay newInstance(String param1, String param2) {
        UserDataDisplay fragment = new UserDataDisplay();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_donation_page, container, false);

        // Initialize your TextViews
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserSurname = view.findViewById(R.id.tvUserSurname);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        tvUserPhoneNumber = view.findViewById(R.id.tvUserPhoneNumber);

        // Fetch user data from SQLite
        fetchUserData();

        return view;
    }

    // temp
    private void fetchUserData() {
        Uri userUri = UserContract.UserEntry.CONTENT_URI;
        String[] projection = {
                UserContract.UserEntry.COLUMN_NAME,
                UserContract.UserEntry.COLUMN_SURNAME,
                UserContract.UserEntry.COLUMN_EMAIL,
                UserContract.UserEntry.COLUMN_PHONE_NUMBER
        };

        Cursor cursor = null;
        try {
            cursor = getActivity().getContentResolver().query(
                    userUri, projection, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_NAME));
                String surname = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_SURNAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_EMAIL));
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.UserEntry.COLUMN_PHONE_NUMBER));

                // Update the UI with fetched data
                //makeToast(name);
                if(name == null){
                    makeToast("null");
                }
                tvUserName.setText(name);
                tvUserSurname.setText(surname);
                tvUserEmail.setText(email);
                tvUserPhoneNumber.setText(phoneNumber);
            }
        } catch (Exception e) {
            // Handle exceptions
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
    private void makeToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}