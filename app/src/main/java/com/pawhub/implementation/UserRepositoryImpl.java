package com.pawhub.implementation;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.pawhub.model.User;
import com.pawhub.repository.UserRepository;
import com.pawhub.utils.Callback;

import java.util.Map;

public class UserRepositoryImpl implements UserRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String COLLECTION = "users";


    @Override
    public void addUser(User user, Callback<Void> callback) {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection(COLLECTION)
                .document(authUser.getUid())
                .set(user)
                .addOnSuccessListener(documentReference -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void getThisUser(Callback<User> callback) {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection(COLLECTION)
                .document(authUser.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            callback.onSuccess(user);
                        } else {
                            callback.onFailure(new Exception("User not found"));
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void getUser(String uid, Callback<User> callback) {
        db.collection(COLLECTION)
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            User user = document.toObject(User.class);
                            callback.onSuccess(user);
                        } else {
                            callback.onFailure(new Exception("User not found"));
                        }
                    } else {
                        callback.onFailure(task.getException());
                    }
                });
    }

    @Override
    public void updateUser(User updatedUser, Callback<Void> callback) {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        db.collection(COLLECTION)
                .document(authUser.getUid())
                .set(updatedUser)
                .addOnSuccessListener(aVoid -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void increaseThisUserPostCount(Callback<Void> callback) {
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference postRef = FirebaseFirestore.getInstance().collection("users").document(authUser.getUid());
        postRef.update("post_count", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    Log.d("PHLOG", "Increase post count");
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    Log.e("PHLOG", "Error occurred when trying to increase post count");
                    callback.onFailure(e);
                });
    }
}
