package com.iashish0001.upload_from_cam_and_gallery;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    MaterialButton galleryButton , camButton;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.image_view);
        galleryButton = (MaterialButton) findViewById(R.id.gallery_button);
        camButton = (MaterialButton) findViewById(R.id.camera_button);

        String RANDOM_AVATAR_IMAGE = "https://doodleipsum.com/500x500/avatar-2?i=a84e32424b61b2c7a0c4e9d6451375c2";
        Glide.with(this)
                .asBitmap()
                .load(RANDOM_AVATAR_IMAGE)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(imageView);



        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    Log.d(TAG, "onActivityResult: " + result.getResultCode());

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            Uri fileUri = result.getData().getData();
                            imageView.setImageURI(fileUri);

                        } else {
                            Log.d(TAG, "onActivityResult: NUll DATA");
                        }
                    } else if (result.getResultCode() == ImagePicker.RESULT_ERROR) {
                        Toast.makeText(MainActivity.this, ImagePicker.getError(result.getData()), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Task Cancelled", Toast.LENGTH_SHORT).show();
                    }


                }
        );

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(MainActivity.this)
                        .crop()
                        .compress(1024)
                        .galleryOnly()
                        .maxResultSize(1080, 1080)
                        .createIntent(intent -> {
                            resultLauncher.launch(intent);
                            return null;
                        });
            }
        });

        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(MainActivity.this)
                        .crop()
                        .compress(1024)
                        .cameraOnly()
                        .maxResultSize(1080, 1080)
                        .createIntent(intent -> {
                            resultLauncher.launch(intent);
                            return null;
                        });
            }
        });


    }
}