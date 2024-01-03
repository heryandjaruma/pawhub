package com.pawhub.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pawhub.AddPawgenicActivity;
import com.pawhub.R;
import com.pawhub.databinding.FragmentProfileBinding;
import com.pawhub.implementation.UserRepositoryImpl;
import com.pawhub.model.Post;
import com.pawhub.model.User;
import com.pawhub.repository.UserRepository;
import com.pawhub.utils.Callback;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileActivity extends AppCompatActivity {
    private EditText username, email;
    private Button buttonSave, buttonCancel;
    private Uri imageUri;
    private ImageView toUpload;
    private FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();
    private UserRepository userRepository = new UserRepositoryImpl();
    private CircleImageView editImageBtt, profileImage;
    private boolean isImageChanged = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        profileImage = findViewById(R.id.profile_image_edit);
        username = findViewById(R.id.etUsername_edit);
        email = findViewById(R.id.etEmail_edit);
        buttonSave = findViewById(R.id.btn_save);
        buttonCancel = findViewById(R.id.btn_cancel);
        editImageBtt = findViewById(R.id.edit_image_button);

        userRepository.getThisUser(new Callback<User>() {
            @Override
            public void onSuccess(User result) {
                username.setText(result.getUsername());
                email.setText(authUser.getEmail());
                imageUri = Uri.parse(result.getProfile_picture());
                Picasso.get().load(imageUri).into(profileImage);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("PHLOG", "Failed to get profile", e);
            }
        });

        editImageBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImageChanged) {
                    uploadImageToFirebaseStorage(imageUri);
                } else {
                    updateUserProfile(null); // No new image, update other profile details
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void selectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    private void updateUserProfile(String imageUrl) {
        userRepository.getThisUser(new Callback<User>() {
            @Override
            public void onSuccess(User result) {
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    result.setProfile_picture(imageUrl);
                }
                result.setUsername(username.getText().toString());

                userRepository.updateUser(result, new Callback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.d("PHLOG", "Update profile success!");
                        Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e("PHLOG", "Failed to update profile", e);
                    }
                });
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("PHLOG", "Failed to update profile", e);
            }
        });
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        if (imageUri == null) {
            Log.e("PHLOG", "No file selected for upload");
            return;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();

        // Generate a unique file name with a .jpg extension
        String fileName = "profile_picture/" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(fileName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload successful
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {

                        userRepository.getThisUser(new Callback<User>() {
                            @Override
                            public void onSuccess(User result) {
                                result.setProfile_picture(downloadUri.toString());
                                result.setUsername(username.getText().toString());
                                userRepository.updateUser(
                                        result,
                                        new Callback<Void>() {
                                            @Override
                                            public void onSuccess(Void result) {
                                                Log.d("PHLOG", "Update profile success!");
                                                Intent intent = new Intent(EditProfileActivity.this, HomeActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onFailure(Exception e) {
                                                Log.e("PHLOG", "Failed to update profile", e);
                                            }
                                        }
                                );
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("PHLOG", "Failed to update profile", e);
                            }
                        });
                        finish();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                    Log.e("PHLOG", "Failed uploading image", e);
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data!=null && data.getData()!=null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profileImage);
            isImageChanged = true;
        }
    }
}