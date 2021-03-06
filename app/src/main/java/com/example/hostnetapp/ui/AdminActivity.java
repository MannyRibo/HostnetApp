package com.example.hostnetapp.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hostnetapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
    private static final String NAAM = "naam";
    TextView naam;
    private static final String IMAGEURL = "imageurl";
    private ImageView profielfoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        naam = findViewById(R.id.naamAdmin);

        profielfoto = findViewById(R.id.adminPicture);
    }

    /* start: check of user documenten heeft in de firebase db, zet Naam en Image op basis van URL */
    @Override
    protected void onStart() {
        super.onStart();
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(AdminActivity.this, "Er ging iets mis met ophalen van data - " +
                                    e.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {
                    naam.setText(documentSnapshot.getString(NAAM));
                    naam.setVisibility(View.VISIBLE);

                    Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/schoolapp-97dd0.appspot.com/o/uploads%2F" + documentSnapshot.getString(IMAGEURL)).into(profielfoto);
                }
            }
        });
    }

    public void onClickEditProfile(View view) {
        Intent intent = new Intent(AdminActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void naarWerknemers(View view) {
        Intent intent = new Intent(AdminActivity.this, AdminResultsActivity.class);
        startActivity(intent);
    }
}