package com.example.hostnetapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hostnetapp.R;
import com.example.hostnetapp.model.Rooster;
import com.example.hostnetapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistreerActivity extends AppCompatActivity {

    private EditText mRegistreerEmailadres;
    private EditText mRegistreerWachtwoord;
    private EditText mRegistreerNaam;
    private EditText mRegistreerTelefoonnummer;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String naam;
    private String emailadres;
    private String wachtwoord;
    private String telefoonnummer;
    private Rooster rooster;
    private int afdeling;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String NAAM = "naam";
    private static final String EMAILADRES = "emailadres";
    private static final String TELEFOONNUMMER = "telefoonnummer";
    private static final String USERID = "userID";
    private static final String ROOSTER = "rooster";
    private static final String AFDELING = "afdeling";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registreer);

        mRegistreerNaam = findViewById(R.id.registreerNaam);
        mRegistreerEmailadres = findViewById(R.id.registreerEmailadres);
        mRegistreerWachtwoord = findViewById(R.id.registreerWachtwoord);
        mRegistreerTelefoonnummer = findViewById(R.id.registreerTelefoonnummer);

    }

    public void updateUI(FirebaseUser user) {
        Toast.makeText(RegistreerActivity.this, "Gebruiker geregistreerd met emailadres " +
                user.getEmail(), Toast.LENGTH_LONG).show();

        Intent intent = new Intent(RegistreerActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickRegistreren(View view) {
        naam = mRegistreerNaam.getText().toString();
        emailadres = mRegistreerEmailadres.getText().toString();
        wachtwoord = mRegistreerWachtwoord.getText().toString();
        telefoonnummer = mRegistreerTelefoonnummer.getText().toString();

        // als emailadres of wachtwoord niet is ingevoerd toast weergeven
        if ((TextUtils.isEmpty(emailadres)) || (TextUtils.isEmpty(wachtwoord))
        || (TextUtils.isEmpty(naam)) || (TextUtils.isEmpty(telefoonnummer))) {
            Toast.makeText(RegistreerActivity.this,
                    "Voer alle velden in", Toast.LENGTH_LONG).show();
        }
        else if ((!TextUtils.isEmpty(emailadres)) || (!TextUtils.isEmpty(wachtwoord))
                || (!TextUtils.isEmpty(naam)) || (!TextUtils.isEmpty(telefoonnummer))) {
            if (!emailadres.contains("@")) {
                Toast.makeText(RegistreerActivity.this,
                        "Voer een geldig e-mailadres in", Toast.LENGTH_LONG).show();
            }

            if (emailadres.contains("@")) {

                int indexApenstaartje = emailadres.indexOf('@');

                String gedeelteNaApenstaartje = emailadres.substring(indexApenstaartje);

                // alleen als het emailadres eindigt op hostnet.nl verder gaan met registreren
                if (!gedeelteNaApenstaartje.equals("@hostnet.nl")) {
                    Toast.makeText(RegistreerActivity.this,
                            "Voer een e-mailadres in dat eindigt op @hostnet.nl", Toast.LENGTH_LONG).show();
                }
                if (gedeelteNaApenstaartje.equals("@hostnet.nl")) {
                    // als emailadres eindigt op @hostnet.nl gebruiker registreren
                    mAuth.createUserWithEmailAndPassword(emailadres, wachtwoord)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // nieuwe gebruiker aanmaken
                                        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                        int afdeling = 1;

                                        rooster = new Rooster(
                                                "09:00 - 17:00", "09:00 - 17:00",
                                                "08:00 - 16:00","10:00 - 18:00",
                                                "09:00 - 17:00","08:00 - 16:00",
                                                "10:00 - 18:00"
                                                );



                                        User user = new User(userID, naam, emailadres, telefoonnummer, rooster, afdeling);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user);

                                        Map<String, Object> newUser = new HashMap<>();
                                        newUser.put(USERID, userID);
                                        newUser.put(NAAM, naamNaarHoofdletters(naam));
                                        newUser.put(EMAILADRES, emailadres);
                                        newUser.put(TELEFOONNUMMER, telefoonnummer);
                                        newUser.put(ROOSTER, rooster);
                                        newUser.put(AFDELING, afdeling);
                                        db.collection("Users").document(
                                                FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .set(newUser);

                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        updateUI(currentUser);

                                    }
                                    else {
                                        Toast.makeText(RegistreerActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        }
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