package com.example.hostnetapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostnetapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class ZoekActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
    public static final String NAAM = "naam";
    public static final String SEARCHNAME = "search";

    private TextView naam;
    private EditText searchNameEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoek);

        searchNameEdit = findViewById(R.id.search_name_edit);
        naam = findViewById(R.id.naamMedewerker);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ZoekActivity.this, "Er ging iets mis met ophalen van data - " +
                            e.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {
                    naam.setText(documentSnapshot.getString(NAAM));
                    naam.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void onClickEditProfile(View view) {
        Intent intent = new Intent(ZoekActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void zoekOpNaam(View view) {
        String searchName = searchNameEdit.getText().toString();
//        Toast.makeText(this, title, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ZoekActivity.this, ResultsActivity.class);
        intent.putExtra("name", searchName);

        startActivity(intent);
    }

    public void naarRooster(View view) {
        Intent intent = new Intent(ZoekActivity.this, RoosterActivity.class);
        startActivity(intent);
    }

}
