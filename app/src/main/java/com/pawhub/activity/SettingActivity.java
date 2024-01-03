package com.pawhub.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.pawhub.R;
import com.pawhub.implementation.AuthRepositoryImpl;
import com.pawhub.repository.AuthRepository;

public class SettingActivity extends AppCompatActivity {

    private AuthRepository authRepository = new AuthRepositoryImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button btnSetting = findViewById(R.id.btn_logout);

        btnSetting.setOnClickListener(view -> performLogout());
    }

    private void performLogout() {
        authRepository.logoutUser();

        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
