package com.example.pawrescue.UserContentProvider;

import com.example.pawrescue.Model.User;
import java.sql.SQLException;

public interface UserRepository {
    void addUser(User user) throws SQLException;
    boolean checkUser(String email, String password) throws SQLException;
    void updateUser(User user) throws SQLException;
    void deleteUser(String email) throws SQLException;
}