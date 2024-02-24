package com.example.pawrescue;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pawrescue.Model.User;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private final List<User> userList;
    private OnItemClickListener listener;

    public RankAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = userList.get(position);
        String fullName = user.getName() + " " + user.getSurname();
        holder.textViewUsername.setText(fullName);
        holder.textViewXP.setText(String.valueOf(user.getXp()));

        // Determine which icon to display based on user's XP
        if (user.getXp() >= 1000) {
            holder.imageViewLeague.setImageResource(R.drawable.ic_league_cutie);
        } else if (user.getXp() >= 500) {
            holder.imageViewLeague.setImageResource(R.drawable.ic_league_gold);
        } else if (user.getXp() >= 100) {
            holder.imageViewLeague.setImageResource(R.drawable.ic_league_silver);
        } else {
            holder.imageViewLeague.setImageResource(R.drawable.ic_league_bronze);
        }

        // Additional logic for top 3 users
        if (position == 0) { // First place
            holder.imageViewLeague.setImageResource(R.drawable.ic_special_icon_1);
        } else if (position == 1) { // Second place
            holder.imageViewLeague.setImageResource(R.drawable.ic_special_icon_2);
        } else if (position == 2) { // Third place
            holder.imageViewLeague.setImageResource(R.drawable.ic_special_icon_3);
        }

        // Set long click listener
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemLongClick(userList.get(position));
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewUsername, textViewXP;
        public ImageView imageViewLeague;

        public ViewHolder(View view) {
            super(view);
            textViewUsername = view.findViewById(R.id.textViewUsername);
            imageViewLeague = view.findViewById(R.id.imageViewLeagueIcon);
            textViewXP = view.findViewById(R.id.textViewXP);
        }
    }

    // Interface for click events
    public interface OnItemClickListener {
        void onItemLongClick(User user);
    }

    // Method to set the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
