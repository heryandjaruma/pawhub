package com.pawhub.repository;

import com.pawhub.model.User;
import com.pawhub.utils.Callback;

import java.util.Map;

public interface UserRepository {
    void addUser(User user, Callback<Void> callback);
    void getUser(Callback<User> callback);
    void updateUser(User updatedUser, Callback<Void> callback);

}
