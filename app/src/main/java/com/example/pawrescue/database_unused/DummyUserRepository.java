package com.example.pawrescue.database_unused;

import com.example.pawrescue.Model.User;

import java.util.ArrayList;

public class DummyUserRepository implements UserRepository {
    private static DummyUserRepository instance = new DummyUserRepository();
    private ArrayList<User> usersList = new ArrayList<>();

    private DummyUserRepository() {
    }

    public static DummyUserRepository getInstance() {
        return instance;
    }

    @Override
    public void addUser(User user) {
        usersList.add(user);
    }

    @Override
    public boolean checkUser(String email, String password) {
        for (User user : usersList) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<User> getUsersList() {
        return usersList;
    }
}
