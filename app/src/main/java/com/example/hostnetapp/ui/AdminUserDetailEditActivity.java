package com.example.hostnetapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostnetapp.R;
import com.example.hostnetapp.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AdminUserDetailEditActivity extends AppCompatActivity {

  private TextView roosterVan;

    private EditText editTextMaandag, editTextDinsdag, editTextWoensdag,
            editTextDonderdag, editTextVrijdag, editTextZaterdag, editTextZondag;

    private ImageView profielfoto;

    private static final String IMAGEURL = "imageurl";

    User user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef;

    private static final String MAANDAG = "maandag";
    private static final String DINSDAG = "dinsdag";
    private static final String WOENSDAG = "woensdag";
    private static final String DONDERDAG = "donderdag";
    private static final String VRIJDAG = "vrijdag";
    private static final String ZATERDAG = "zaterdag";
    private static final String ZONDAG = "zondag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details_edit);

        user = getIntent().getParcelableExtra(AdminResultsActivity.USER);

        userRef = db.collection("Users").document(user.getUserID());

        editTextMaandag = findViewById(R.id.editTextTijdMaandag);
        editTextDinsdag = findViewById(R.id.editTextTijdDinsdag);
        editTextWoensdag = findViewById(R.id.editTextTijdWoensdag);
        editTextDonderdag = findViewById(R.id.editTextTijdDonderdag);
        editTextVrijdag = findViewById(R.id.editTextTijdVrijdag);
        editTextZaterdag = findViewById(R.id.editTextTijdZaterdag);
        editTextZondag = findViewById(R.id.editTextTijdZondag);
        roosterVan = findViewById(R.id.profielVan_details);

        roosterVan.setText(getString(R.string.roostervan, user.getNaam()));
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(AdminUserDetailEditActivity.this, "Er ging iets mis met ophalen van data - " +
                                    e.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {

                    User newUser = (documentSnapshot.toObject(User.class));

                    editTextMaandag.setText(newUser.getRooster().getMaandag());
                    editTextDinsdag.setText(newUser.getRooster().getDinsdag());
                    editTextWoensdag.setText(newUser.getRooster().getWoensdag());
                    editTextDonderdag.setText(newUser.getRooster().getDonderdag());
                    editTextVrijdag.setText(newUser.getRooster().getVrijdag());
                    editTextZaterdag.setText(newUser.getRooster().getZaterdag());
                    editTextZondag.setText(newUser.getRooster().getZondag());
                }

            }
        });
    }

    public void saveRooster(View view) {

        String maandag = editTextMaandag.getText().toString();
        String dinsdag = editTextDinsdag.getText().toString();
        String woensdag = editTextWoensdag.getText().toString();
        String donderdag = editTextDonderdag.getText().toString();
        String vrijdag = editTextVrijdag.getText().toString();
        String zaterdag = editTextZaterdag.getText().toString();
        String zondag = editTextZondag.getText().toString();

        Map<String, Object> rooster = new HashMap<>();
        Map<String, Object> nieuwRooster = new HashMap<>();
        nieuwRooster.put(MAANDAG, maandag);
        nieuwRooster.put(DINSDAG, dinsdag);
        nieuwRooster.put(WOENSDAG, woensdag);
        nieuwRooster.put(DONDERDAG, donderdag);
        nieuwRooster.put(VRIJDAG, vrijdag);
        nieuwRooster.put(ZATERDAG, zaterdag);
        nieuwRooster.put(ZONDAG, zondag);
        rooster.put("rooster", nieuwRooster);
        userRef.set(rooster, SetOptions.merge());

        Toast.makeText(this, "Gegevens bijgewerkt", Toast.LENGTH_SHORT).show();
    }
}