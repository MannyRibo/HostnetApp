package com.example.hostnetapp.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class RegistreerActivity extends AppCompatActivity {

    private EditText mRegistreerEmailadres;
    private EditText mRegistreerWachtwoord;
    private EditText mRegistreerNaam;
    private EditText mRegistreerTelefoonnummer;
    private Spinner mAfdeling;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String naam;
    private String emailadres;
    private String wachtwoord;
    private String telefoonnummer;
    private Rooster rooster;
    private String afdeling;
    private String imageurl;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String NAAM = "naam";
    private static final String EMAILADRES = "emailadres";
    private static final String TELEFOONNUMMER = "telefoonnummer";
    private static final String USERID = "userID";
    private static final String ROOSTER = "rooster";
    private static final String AFDELING = "afdeling";
    private static final String IMAGEURL = "imageurl";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registreer);

        mRegistreerNaam = findViewById(R.id.registreerNaam);
        mRegistreerEmailadres = findViewById(R.id.registreerEmailadres);
        mRegistreerWachtwoord = findViewById(R.id.registreerWachtwoord);
        mRegistreerTelefoonnummer = findViewById(R.id.registreerTelefoonnummer);
        mAfdeling = (Spinner) findViewById(R.id.spinner_edit);
        addItemsOnSpinner();

        // spinner laten verdwijnen als emailadres admin@hostnet.nl is
        mRegistreerTelefoonnummer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                emailadres = mRegistreerEmailadres.getText().toString();

                if (MotionEvent.ACTION_UP == event.getAction()) {
                    if (emailadres != null) {
                        if (emailadres.equals("admin@hostnet.nl")) {
                            mAfdeling.setVisibility(View.INVISIBLE);
                        }
                        if (!emailadres.equals("admin@hostnet.nl")) {
                            mAfdeling.setVisibility(View.VISIBLE);
                        }
                    }
                }
                return false;
            }
        });

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
        afdeling = mAfdeling.getSelectedItem().toString();
//        Toast.makeText(this, afdeling, Toast.LENGTH_SHORT).show();

        // admin hoeft geen afdeling te kiezen, deze gaat standaard naar afdeling admin
        if (afdeling == "Kies je afdeling..." && emailadres == "admin@hostnet.nl"
                && (!TextUtils.isEmpty(wachtwoord)) && (!TextUtils.isEmpty(naam))
                && (!TextUtils.isEmpty(telefoonnummer))) {

            gebruikerRegistreren();
        }

        // als niet alle velden zijn ingevoerd toast weergeven
        if ((TextUtils.isEmpty(emailadres)) || (TextUtils.isEmpty(wachtwoord))
                || (TextUtils.isEmpty(naam)) || (TextUtils.isEmpty(telefoonnummer)) || afdeling == "Kies je afdeling...") {
            Toast.makeText(RegistreerActivity.this,
                    "Voer alle velden in", Toast.LENGTH_LONG).show();

        } else if ((!TextUtils.isEmpty(emailadres)) || (!TextUtils.isEmpty(wachtwoord))
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

                    gebruikerRegistreren();

                }
            }
        }
    }

    private void gebruikerRegistreren() {
        mAuth.createUserWithEmailAndPassword(emailadres, wachtwoord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // nieuwe gebruiker aanmaken
                            String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            String imageurl = String.valueOf(R.drawable.profilepicture);
                            System.out.println("liewe "+imageurl);

                            User newUser;

                            rooster = new Rooster(
                                    "09:00 - 17:00", "09:00 - 17:00",
                                    "08:00 - 16:00", "10:00 - 18:00",
                                    "09:00 - 17:00", "Vrij",
                                    "Vrij"
                            );

                            // als emailadres admin@hostnet.nl is afdeling admin maken,
                            // dan is ie ook niet terug te vinden in recyclerview
                            if (emailadres.equals("admin@hostnet.nl")) {
                                newUser = new User(userID, naamNaarHoofdletters(naam), emailadres, telefoonnummer, rooster, "Admin", imageurl);
                            } else {
                                newUser = new User(userID, naamNaarHoofdletters(naam), emailadres, telefoonnummer, rooster, afdeling, imageurl);
                            }

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(newUser);

                            db.collection("Users").document(
                                    FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .set(newUser);

                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            updateUI(currentUser);

                        } else {
                            Toast.makeText(RegistreerActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
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

    // add items into spinner dynamically
    public void addItemsOnSpinner() {
        mAfdeling = (Spinner) findViewById(R.id.spinner_edit);
        String[] afdelingen = getResources().getStringArray(R.array.afdelingen);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, afdelingen) {
            //grijs maken van de voorselectie op de spinner nadat erop is geklikt
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAfdeling.setAdapter(dataAdapter);

    }

}