package com.pawhub;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pawhub.implementation.PostRepositoryImpl;
import com.pawhub.model.Post;
import com.pawhub.repository.PostRepository;
import com.pawhub.utils.Callback;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.UUID;

public class AddPawgenicActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int READ_MEDIA_IMAGES_PERMISSION_CODE = 100;
    StorageReference storageReference;
    private PostRepository postRepository = new PostRepositoryImpl();

    private ImageView toUpload;
    private Button cancelButton, uploadButton;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pawgenic);

        toUpload = findViewById(R.id.to_upload_image);
        cancelButton = findViewById(R.id.choose_image_btn);
        uploadButton = findViewById(R.id.upload_image_button);

        selectImage();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebaseStorage(imageUri);
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

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        if (imageUri == null) {
            Log.e("PHLOG", "No file selected for upload");
            return;
        }

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();

        // Generate a unique file name with a .jpg extension
        String fileName = "images/" + System.currentTimeMillis() + ".jpg";
        StorageReference imageRef = storageRef.child(fileName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload successful
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        // Use this download URL as needed
                        postRepository.addPost(new Post(
                                UUID.randomUUID().toString(),
                                authUser.getUid(),
                                new Timestamp(new Date()),
                                downloadUri.toString(),
                                0
                        ), new Callback<Void>() {
                            @Override
                            public void onSuccess(Void result) {
                                Log.d("PHLOG", "Success uploading image: " + downloadUri.toString());
                                Toast.makeText(AddPawgenicActivity.this, "Success uploading your Pawgenic!", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.e("PHLOG", "Failed uploading image", e);
                            }
                        });

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
            Picasso.get().load(imageUri).into(toUpload);
        }
    }

}