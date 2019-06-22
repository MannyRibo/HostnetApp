package com.example.hostnetapp.ui;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostnetapp.R;
import com.example.hostnetapp.model.Rooster;
import com.example.hostnetapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.model.value.FieldValue;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AdminUserDetailEditActivity extends AppCompatActivity {

    private TextView naam, afdeling, emailadres, telefoonnummer, profielNaam;

    private EditText editTextMaandag, editTextDinsdag, editTextWoensdag,
            editTextDonderdag, editTextVrijdag, editTextZaterdag, editTextZondag;

    private ImageView profielfoto;

    private static final String IMAGEURL = "imageurl";

    User user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef;

    public static final String USER = "user";
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

        naam = findViewById(R.id.naam_details_text);
        afdeling = findViewById(R.id.afdeling_details_text);
        emailadres = findViewById(R.id.email_details_text);
        telefoonnummer = findViewById(R.id.telefoonnummer_details_text);
        editTextMaandag = findViewById(R.id.editTextTijdMaandag);
        editTextDinsdag = findViewById(R.id.editTextTijdDinsdag);
        editTextWoensdag = findViewById(R.id.editTextTijdWoensdag);
        editTextDonderdag = findViewById(R.id.editTextTijdDonderdag);
        editTextVrijdag = findViewById(R.id.editTextTijdVrijdag);
        editTextZaterdag = findViewById(R.id.editTextTijdZaterdag);
        editTextZondag = findViewById(R.id.editTextTijdZondag);
        profielNaam = findViewById(R.id.profielVan_details);
        profielfoto = findViewById(R.id.profielfoto_details);

        profielNaam.setText(getString(R.string.profielvan, user.getNaam()));
        naam.setText(user.getNaam());
        afdeling.setText(user.getAfdeling());
        emailadres.setText(user.getEmailadres());
        telefoonnummer.setText(user.getTelefoonnummer());
        editTextMaandag.setText(user.getRooster().getMaandag());
        editTextDinsdag.setText(user.getRooster().getDinsdag());
        editTextWoensdag.setText(user.getRooster().getWoensdag());
        editTextDonderdag.setText(user.getRooster().getDonderdag());
        editTextVrijdag.setText(user.getRooster().getVrijdag());
        editTextZaterdag.setText(user.getRooster().getZaterdag());
        editTextZondag.setText(user.getRooster().getZondag());
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

                    profielfoto.setImageDrawable(Drawable.createFromPath(documentSnapshot.getString(IMAGEURL)));
                    profielfoto.setVisibility(View.VISIBLE);
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