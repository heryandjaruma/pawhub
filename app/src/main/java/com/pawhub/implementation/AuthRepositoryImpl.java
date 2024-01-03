package com.pawhub.implementation;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pawhub.model.User;
import com.pawhub.repository.AuthRepository;
import com.pawhub.repository.UserRepository;
import com.pawhub.utils.Callback;

public class AuthRepositoryImpl implements AuthRepository {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private UserRepository userRepository = new UserRepositoryImpl();

    @Override
    public void registerUser(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("PHLOG", "createUserWithEmail:success");
                        userRepository.addUser(
                                new User(
                                        username,
                                        "https://firebasestorage.googleapis.com/v0/b/pawhub-3c87a.appspot.com/o/warcat.jpg?alt=media&token=53513ffd-a860-4c2c-9e7a-065d058955d6",
                                        0
                                        ),
                                new Callback<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                                Log.d("PHLOG", "Success adding user profile to database");
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("PHLOG", "Failed adding user profile to database");
                            }
                        });
                    } else {
                        Log.e("PHLOG", "createUserWithEmail:failure", task.getException());
                    }
                });
    }

    @Override
    public void loginUser(String email, String password, Callback<Boolean> callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("PHLOG", String.format("Logged in as %s", email));
                        callback.onSuccess(true);
                    } else {
                        Log.w("PHLOG", "Failed to login", task.getException());
                        callback.onFailure(new Exception("Failed to login"));
                    }
                });
    }

    @Override
    public void logoutUser() {
        mAuth.signOut();
    }
}
