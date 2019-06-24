package com.example.hostnetapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

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
    private static String imageUrl;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    public static final int REQUEST_PERMISSION = 200;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri mImageUri;
    private StorageTask mUploadTask;

    private String currentPhotoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profielNaam = findViewById(R.id.profielVan);
        telefoonnummer = findViewById(R.id.search_name_edit);
        editProfielNaam = findViewById(R.id.editProfielnaam);
        profileImage = findViewById(R.id.profile_view_edit);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users"); //.child(mAuth.getCurrentUser().getUid());
//
//        mDatabaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                    } else {
                        Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/schoolapp-97dd0.appspot.com/o/uploads%2F" + documentSnapshot.getString(IMAGEURL)).into(profileImage);
                        //"https://firebasestorage.googleapis.com/v0/b/schoolapp-97dd0.appspot.com/o/uploads/"

//                        profileImage.setImageDrawable(Drawable.createFromPath(documentSnapshot.getString(IMAGEURL)));
//                        profileImage.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void onClickSaveProfile(View view) {
        String nieuweNaam = editProfielNaam.getText().toString();
        String nieuwTelefoonnummer = telefoonnummer.getText().toString();
//        String nieuwProfileImage = currentPhotoPath;
        System.out.println("onclick   " + imageUrl);
//
//                +imageUrl).into(profileImage);

        Map<String, Object> nieuweGegevens = new HashMap<>();
        nieuweGegevens.put(NAAM, naamNaarHoofdletters(nieuweNaam));
        nieuweGegevens.put(TELEFOONNUMMER, nieuwTelefoonnummer);
        nieuweGegevens.put(IMAGEURL, imageUrl);
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
            uploadFile(currentPhotoPath);

        }
    }

    public void onClickChangePicture(View view) {
        openCameraIntent();
    }

//    private String getFileExtension(Uri uri) {
//        ContentResolver cR = getContentResolver();
//        MimeTypeMap mime = MimeTypeMap.getSingleton();
//        return mime.getExtensionFromMimeType(cR.getType(uri));
//    }

    private void uploadFile(final String currentPhotoPath) {
        String substring = currentPhotoPath.substring(1);
        System.out.println(substring);
        Uri file = Uri.fromFile(new File(currentPhotoPath));
        System.out.println(mAuth.getCurrentUser().getUid());
        final StorageReference riversRef = mStorageRef.child(mAuth.getCurrentUser().getUid() + ".jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imageUrl = uri.toString().substring(84);
                                        Glide.with(getApplicationContext()).load(currentPhotoPath).into(profileImage);

                                    }
                                })
//
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get a URL to the uploaded content
////                        IMAGEURL = riversRef.toString().substring(5);
////                        System.out.println("DL URL   "+riversRef.toString().substring(5));
//
//                        IMAGEURL = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
////                        Glide.with(this).load(IMAGEURL).into(profileImage);
//                        System.out.println(IMAGEURL);
////                        profileImage.setImageResource();
////                        profileImage.setVisibility(View.VISIBLE);
//                    }
//                })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception exception) {
                                                // Handle unsuccessful uploads
                                                // ...
                                            }
                                        });

                            }
                        }
                    }
                });
    }
}