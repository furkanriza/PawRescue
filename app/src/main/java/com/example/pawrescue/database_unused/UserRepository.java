package com.example.pawrescue.database_unused;

import com.example.pawrescue.Model.User;

public interface UserRepository {
    void addUser(User user);
    boolean checkUser(String email, String password);
    // Other methods can be defined as needed
}

