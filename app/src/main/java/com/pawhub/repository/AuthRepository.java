package com.pawhub.repository;

import java.util.Map;

public interface AuthRepository {
    void registerUser(String email, String password, String username);
    void loginUser(String email, String password);
    void logoutUser();
}
