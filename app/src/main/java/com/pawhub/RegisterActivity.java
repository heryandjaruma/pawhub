package com.pawhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.pawhub.implementation.AuthRepositoryImpl;
import com.pawhub.model.Post;
import com.pawhub.implementation.PostRepositoryImpl;
import com.pawhub.repository.AuthRepository;
import com.pawhub.utils.Callback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    EditText etUsername, etEmail, etPassword, etPasswordConfirm;
    Button btnCreateAccount, btnSignIn;
    TextView tvError;
    AuthRepository authRepository = new AuthRepositoryImpl();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnCreateAccount = findViewById(R.id.createAccountBtn);
        btnSignIn = findViewById(R.id.signInBtn);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordConfirm = findViewById(R.id.etPasswordConfirm);

        tvError = findViewById(R.id.errorText);

        btnSignIn.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        btnCreateAccount.setOnClickListener(view -> {
            if (validateRegister()) {
                authRepository.registerUser(
                        etEmail.getText().toString(),
                        etPassword.getText().toString(),
                        etUsername.getText().toString()
                );
                Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    protected boolean validateRegister() {
        if (etUsername.getText().toString().isEmpty()){
            tvError.setText(R.string.empty_username_error);
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        if (etEmail.getText().toString().isEmpty()){
            tvError.setText(R.string.empty_email_error);
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        if (etPassword.getText().toString().isEmpty()){
            tvError.setText(R.string.empty_password_error);
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        if (etPassword.getText().toString().length() < 6){
            tvError.setText(R.string.password_length_error);
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        if (etPasswordConfirm.getText().toString().isEmpty()){
            tvError.setText(R.string.empty_password_confirmation_error);
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        String password = etPassword.getText().toString();
        String confirmPassword = etPasswordConfirm.getText().toString();

        if (!confirmPassword.equals(password)) {
            tvError.setText(R.string.password_not_match_error);
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        tvError.setVisibility(View.INVISIBLE);
        tvError.setText("");
        return true;
    }

//    private void getPost(String documentId) {
//        postRepository.getPost(documentId, new Callback<Post>() {
//            @Override
//            public void onSuccess(Post result) {}
//
//            @Override
//            public void onFailure(Exception e) {
//                Toast.makeText(RegisterActivity.this, "Error getting a post", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void addPost(Post post) {
//        postRepository.addPost(post, new Callback<Void>() {
//            @Override
//            public void onSuccess(Void result) {
//                Toast.makeText(RegisterActivity.this, "Posted!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Toast.makeText(RegisterActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}