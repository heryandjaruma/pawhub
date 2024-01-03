package com.pawhub.repository;

import com.pawhub.utils.Callback;

import java.util.Map;

public interface AuthRepository {
    void registerUser(String email, String password, String username);
    void loginUser(String email, String password, Callback<Boolean> callback);
    void logoutUser();
}
