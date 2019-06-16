package com.example.hostnetapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.hostnetapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ZoekActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
    private static final String NAAM = "naam";
    TextView naam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoek);

        naam = findViewById(R.id.naamMedewerker);
        naamInstellen();
    }

    public void onClickEditProfile(View view) {
        Intent intent = new Intent(ZoekActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void imageViewZoekNaam(View view) {
        Intent intent = new Intent(ZoekActivity.this, ResultsActivity.class);
        startActivity(intent);
    }

    public void naamInstellen() {
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            naam.setText(documentSnapshot.getString(NAAM));
                            naam.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
