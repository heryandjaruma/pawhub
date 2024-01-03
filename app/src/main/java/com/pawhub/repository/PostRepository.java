package com.pawhub.repository;

import com.pawhub.model.Post;
import com.pawhub.utils.Callback;

import java.util.List;

public interface PostRepository {
    void addPost(Post post, Callback<Void> callback);
    void getPost(String documentId, Callback<Post> callback);
    void getAllPosts(Callback<List<Post>> callback);
    void updateLikeCount(String postId);
    void getUserPosts(String uid, Callback<List<Post>> callback);
}

