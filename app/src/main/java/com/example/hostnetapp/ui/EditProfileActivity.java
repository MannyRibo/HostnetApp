package com.example.hostnetapp.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class EditProfileActivity extends AppCompatActivity {

    private TextView profielNaam;
    private EditText telefoonnummer;
    private EditText editProfielNaam;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
    private static final String NAAM = "naam";
    private static final String TELEFOONNUMMER = "telefoonnummer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        profielNaam = findViewById(R.id.profielVan);
        telefoonnummer = findViewById(R.id.search_name_edit);
        editProfielNaam = findViewById(R.id.editProfielnaam);
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
                    profielNaam.setText("Profiel van " + documentSnapshot.getString(NAAM));
                    profielNaam.setVisibility(View.VISIBLE);
                    editProfielNaam.setText(documentSnapshot.getString(NAAM));
                    telefoonnummer.setText(documentSnapshot.getString(TELEFOONNUMMER));
                }
            }
        });
    }

    public void onClickSaveProfile(View view) {

        String nieuweNaam = editProfielNaam.getText().toString();
        String nieuwTelefoonnummer = telefoonnummer.getText().toString();

        Map<String, Object> nieuweGegevens = new HashMap<>();
        nieuweGegevens.put(NAAM, naamNaarHoofdletters(nieuweNaam));
        nieuweGegevens.put(TELEFOONNUMMER, nieuwTelefoonnummer);
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
}
