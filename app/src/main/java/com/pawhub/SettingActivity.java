package com.pawhub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.pawhub.implementation.PostRepositoryImpl;
import com.pawhub.model.Post;
import com.pawhub.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private PostRepositoryImpl postRepository;
    private ArrayList<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        postRepository = new PostRepositoryImpl();
        posts = new ArrayList<>(); // Initialize the list
        loadPosts();
    }

    private void loadPosts() {
        postRepository.getAllPosts(new Callback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> result) {
                posts.addAll(result);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(HomeActivity.this, "Error loading posts", Toast.LENGTH_SHORT).show();
            }
        });
    }
}