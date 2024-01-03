package com.pawhub.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pawhub.implementation.AuthRepositoryImpl;
import com.pawhub.repository.AuthRepository;
import com.pawhub.utils.Callback;

import com.pawhub.R;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    Button btnCreateAccount, btnSignIn;
    TextView tvError;
    AuthRepository authRepository = new AuthRepositoryImpl();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnCreateAccount = findViewById(R.id.createAccountBtn);
        btnSignIn = findViewById(R.id.signInBtn);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvError = findViewById(R.id.errorText);

        btnCreateAccount.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        btnSignIn.setOnClickListener(view -> {
            if (validateLogin()) {
                authRepository.loginUser(
                        etEmail.getText().toString(),
                        etPassword.getText().toString(),
                        new Callback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean result) {
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                tvError.setText(R.string.login_failed_error);
                                tvError.setVisibility(View.VISIBLE);
                            }
                        }
                );
            }
        });
    }

    protected boolean validateLogin() {
        if (etEmail.getText().toString().isEmpty()){
            tvError.setText(R.string.empty_fields_error);
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        if (etPassword.getText().toString().isEmpty()){
            tvError.setText(R.string.empty_fields_error);
            tvError.setVisibility(View.VISIBLE);
            return false;
        }

        tvError.setVisibility(View.INVISIBLE);
        tvError.setText("");
        return true;
    }
}