package com.pawhub.implementation;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.pawhub.model.Post;
import com.pawhub.repository.PostRepository;
import com.pawhub.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class PostRepositoryImpl implements PostRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String COLLECTION = "posts";

    @Override
    public void addPost(Post post, Callback<Void> callback) {
        db.collection(COLLECTION)
                .document(post.getPost_id())
                .set(post)
                .addOnSuccessListener(documentReference -> callback.onSuccess(null))
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void getPost(String documentId, Callback<Post> callback) {
        db.collection(COLLECTION).document(documentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Post post = documentSnapshot.toObject(Post.class);
                        callback.onSuccess(post);
                    } else {
                        callback.onFailure(new Exception("Post not found"));
                    }
                })
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void getAllPosts(Callback<List<Post>> callback) {
//        db.collection(COLLECTION).get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    List<Post> posts = queryDocumentSnapshots.toObjects(Post.class);
//                    callback.onSuccess(posts);
//                })
//                .addOnFailureListener(callback::onFailure);
        db.collection(COLLECTION).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        post.setPost_id(documentSnapshot.getId());
                        posts.add(post);
                    }
                    callback.onSuccess(posts);
                })
                .addOnFailureListener(callback::onFailure);
    }

    @Override
    public void updateLikeCount(String postId, Callback<Void> callback) {
        DocumentReference postRef = FirebaseFirestore.getInstance().collection("posts").document(postId);
        postRef.update("like_count", FieldValue.increment(1))
                .addOnSuccessListener(aVoid -> {
                    Log.d("PHLOG", "Liked a post");
                    callback.onSuccess(null);
                })
                .addOnFailureListener(e -> {
                    Log.e("PHLOG", "Error occurred when trying to like a post");
                    callback.onFailure(e);
                });
    }

    @Override
    public void getUserPosts(String uid, Callback<List<Post>> callback) {
        db.collection(COLLECTION)
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Post> posts = queryDocumentSnapshots.toObjects(Post.class);
                    callback.onSuccess(posts);
                })
                .addOnFailureListener(callback::onFailure);
    }
}
