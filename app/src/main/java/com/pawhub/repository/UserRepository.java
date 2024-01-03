package com.pawhub.repository;

import com.pawhub.model.User;
import com.pawhub.utils.Callback;

public interface UserRepository {
    void addUser(User user, Callback<Void> callback);
    void getThisUser(Callback<User> callback);
    void getUser(String uid, Callback<User> callback);
    void updateUser(User updatedUser, Callback<Void> callback);

}
