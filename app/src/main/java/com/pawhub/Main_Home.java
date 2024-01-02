package com.pawhub;

import static com.pawhub.R.id.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.pawhub.databinding.ActivityMainHomeBinding;

public class Main_Home extends AppCompatActivity {
    ActivityMainHomeBinding binding;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
//                case R.id.home:
//                    replaceFragment(new HomeFragment());
//                    break;
//                case R.id.add:
//                    replaceFragment(new AddPhotoFragment());
//                    break;
//                case R.id.profile:
//                    replaceFragment(new ProfileFragment());
//                    break;
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