package com.pawhub.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.pawhub.PostsAdapter;
import com.pawhub.R;
import com.pawhub.databinding.ActivityHomeBinding;
import com.pawhub.fragment.AddPhotoFragment;
import com.pawhub.fragment.HomeFragment;
import com.pawhub.fragment.ProfileFragment;
import com.pawhub.implementation.PostRepositoryImpl;
import com.pawhub.model.Post;
import com.pawhub.repository.PostRepository;
import com.pawhub.utils.Callback;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    ActivityHomeBinding binding;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.add) {
                replaceFragment(new AddPhotoFragment());
            } else if (itemId == R.id.profile) {
                replaceFragment(new ProfileFragment());
            }

            return true;

        });

    }


    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_lay, fragment);
        fragmentTransaction.commit();
    }

}

