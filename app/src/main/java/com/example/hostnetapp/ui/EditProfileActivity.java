package com.example.hostnetapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hostnetapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class EditProfileActivity extends AppCompatActivity {

    private TextView profielNaam;
    private EditText telefoonnummer;
    private EditText editProfielNaam;
    private ImageView profileImage;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
    private static final String NAAM = "naam";
    private static final String TELEFOONNUMMER = "telefoonnummer";
    private static final String IMAGEURL = "imageurl";
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;

    private String currentPhotoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profielNaam = findViewById(R.id.profielVan);
        telefoonnummer = findViewById(R.id.search_name_edit);
        editProfielNaam = findViewById(R.id.editProfielnaam);
        profileImage = findViewById(R.id.profile_view_edit);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(EditProfileActivity.this, "Er ging iets mis met ophalen van data - " +
                                    e.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {
                    profielNaam.setText(getString(R.string.profielvan, documentSnapshot.getString(NAAM)));
                    profielNaam.setVisibility(View.VISIBLE);
                    editProfielNaam.setText(documentSnapshot.getString(NAAM));
                    telefoonnummer.setText(documentSnapshot.getString(TELEFOONNUMMER));

                    if (documentSnapshot.getString(IMAGEURL) == null) {
                        profileImage.setImageResource(R.drawable.profilepicture);
                        profileImage.setVisibility(View.VISIBLE);
                    }
                    else {
                        profileImage.setImageDrawable(Drawable.createFromPath(documentSnapshot.getString(IMAGEURL)));
                        profileImage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void onClickSaveProfile(View view) {
        String nieuweNaam = editProfielNaam.getText().toString();
        String nieuwTelefoonnummer = telefoonnummer.getText().toString();
        String nieuwProfileImage = currentPhotoPath;

        Map<String, Object> nieuweGegevens = new HashMap<>();
        nieuweGegevens.put(NAAM, naamNaarHoofdletters(nieuweNaam));
        nieuweGegevens.put(TELEFOONNUMMER, nieuwTelefoonnummer);
        nieuweGegevens.put(IMAGEURL, nieuwProfileImage);
        userRef.update(nieuweGegevens);

        Toast.makeText(this, "Gegevens bijgewerkt", Toast.LENGTH_SHORT).show();
    }

    public String naamNaarHoofdletters(String naam) {
        naam.trim();
        String[] arr = naam.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        String naamHoofdletters = sb.toString().trim();

        return naamHoofdletters;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        System.out.println("liewe "+currentPhotoPath);
        return image;
    }

    private void openCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "no file", Toast.LENGTH_SHORT).show();

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.hostnetapp",
                        photoFile);
//                Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".share", result);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAPTURE_IMAGE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == REQUEST_CAPTURE_IMAGE) {
            //don't compare the data to null, it will always come as  null because we are providing a file URI, so load with the imageFilePath we obtained before opening the cameraIntent
            Glide.with(this).load(currentPhotoPath).into(profileImage);
        }
    }

    public void onClickChangePicture(View view) {
        openCameraIntent();
    }

}
