package com.example.pawrescue;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pawrescue.Model.User;

import java.util.ArrayList;
import java.util.List;

public class RankPage extends Fragment {

    private RecyclerView recyclerView;
    private RankAdapter adapter;
    private List<User> userList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank_page, container, false);
        recyclerView = view.findViewById(R.id.rankRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();
        initializeSampleUsers();

        adapter = new RankAdapter(userList);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new RankAdapter.OnItemClickListener() {
            @Override
            public void onItemLongClick(User user) {
                navigateToUserProfile(user);
            }
        });

        return view;
    }

    private void navigateToUserProfile(User user) {
        UserProfilePage userProfilePage = new UserProfilePage();
        Bundle bundle = new Bundle();

        userProfilePage.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, userProfilePage)
                .addToBackStack(null)
                .commit();
    }

    private void initializeSampleUsers() {
        userList.add(new User("Alice", "Smith", "555-0100", "alice@example.com", "pass123", "Gold League", 1200));
        userList.add(new User("Bob", "Johnson", "555-0101", "bob@example.com", "pass456", "Silver League", 800));
    }
}
