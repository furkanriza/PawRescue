package com.example.pawrescue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pawrescue.Model.Donation;

import java.util.List;

public class DonationAdapter extends ArrayAdapter<Donation> {
    public DonationAdapter(Context context, List<Donation> donations) {
        super(context, 0, donations);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item_donation, parent, false);
        }

        // Get the current donation item
        Donation donation = getItem(position);

        // Populate the TextViews in the custom row layout
        TextView tvDate = convertView.findViewById(R.id.tvDate);
        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvAmount = convertView.findViewById(R.id.tvAmount);

        // Set the values for each column
        if (donation != null) {
            tvDate.setText(donation.getDate());
            tvName.setText(donation.getUserNameAndSurname());
            tvAmount.setText(donation.getAmount());
        }
        return convertView;
    }
}
