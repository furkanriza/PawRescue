package com.example.pawrescue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pawrescue.Model.User;

public class UserProfilePage extends Fragment {

    private User user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile_page, container, false);

        if (user != null) {
            TextView textViewName = view.findViewById(R.id.textViewName);
            TextView textViewSurname = view.findViewById(R.id.textViewSurname);
            TextView textViewEmail = view.findViewById(R.id.textViewEmail);
            TextView textViewPhone = view.findViewById(R.id.textViewPhone);
            TextView textViewLeague = view.findViewById(R.id.textViewLeague);
            TextView textViewXP = view.findViewById(R.id.textViewXP);

            textViewName.setText(user.getName());
            textViewSurname.setText(user.getSurname());
            textViewEmail.setText(user.getEmail());
            textViewPhone.setText(user.getPhoneNumber());
            textViewLeague.setText(user.getLeague());
            textViewXP.setText(String.valueOf(user.getXp()));
        }

        return view;
    }
}
