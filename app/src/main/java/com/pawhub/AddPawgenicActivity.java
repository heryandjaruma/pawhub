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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AddPawgenicActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int READ_MEDIA_IMAGES_PERMISSION_CODE = 100;
    StorageReference storageReference;

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

//    private void openImageChooser() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(intent, PICK_IMAGE_REQUEST);
//    }

    private void selectImage()
    {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child("images/" + imageUri.getLastPathSegment());

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload successful
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        // Use this download URL as needed
                        Log.d("PHLOG","Succes uploading image");
                        finish();
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle unsuccessful uploads
                    // ...
                    Log.e("PHLOG","Failed uploading image", e);
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