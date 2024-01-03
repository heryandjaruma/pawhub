package com.pawhub.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.pawhub.R;
import com.pawhub.repository.AuthRepository;


import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private AuthRepository authRepository;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ListView listView = findViewById(R.id.opt_setting);

        ArrayList<String> dataList = new ArrayList<>();
        dataList.add("Log Out");
        dataList.add("Privacy & Policy");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                if ("Log Out".equals(selectedItem)) {
                    showLogoutConfirmationDialog();
                } else {
                    Toast.makeText(SettingActivity.this,"Selected:" + selectedItem, Toast.LENGTH_SHORT).show();
                }

        }

        private void showLogoutConfirmationDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
            builder.setTitle("Log Out Confirmation");
            builder.setMessage("Are you sure you want to log out?");

            builder.setPositiveButton("Log Out", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    performLogout();
                }
            });
        }

        private void performLogout(){
            authRepository.logoutUser();

            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        };

    });
}}
