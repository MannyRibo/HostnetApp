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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mEmailadres;
    private EditText mWachtwoord;
    String emailadres;
    String wachtwoord;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmailadres = findViewById(R.id.emailadres);
        mWachtwoord = findViewById(R.id.wachtwoord);

    }

    /* inloggen: check of velden zijn ingevuld. Sign in en ga naar Zoek Activiteit */
    public void onClickInloggen(View view) {

        emailadres = mEmailadres.getText().toString();
        wachtwoord = mWachtwoord.getText().toString();

        // als emailadres of wachtwoord niet is ingevoerd toast weergeven
        if ((TextUtils.isEmpty(emailadres)) || (TextUtils.isEmpty(wachtwoord))) {
            Toast.makeText(MainActivity.this,
                    "Voer een e-mailadres en wachtwoord in", Toast.LENGTH_LONG).show();
        }

        else if ((!TextUtils.isEmpty(emailadres)) || (!TextUtils.isEmpty(wachtwoord))) {
            if (!emailadres.contains("@")) {
                Toast.makeText(MainActivity.this,
                        "Voer een geldig e-mailadres in", Toast.LENGTH_LONG).show();
            }

            if (emailadres.contains("@")) {

                int indexApenstaartje = emailadres.indexOf('@');

                String gedeelteNaApenstaartje = emailadres.substring(indexApenstaartje);

                // alleen als het emailadres eindigt op hostnet.nl verder gaan met inloggen
                if (!gedeelteNaApenstaartje.equals("@hostnet.nl")) {
                    Toast.makeText(MainActivity.this,
                            "Voer een e-mailadres in dat eindigt op @hostnet.nl", Toast.LENGTH_LONG).show();
                }
                if (gedeelteNaApenstaartje.equals("@hostnet.nl")) {

                    mAuth.signInWithEmailAndPassword(emailadres, wachtwoord)

                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // inloggen gelukt, update UI met informatie van de gebruiker
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI();
                                    } else {
                                        // Als inloggen mislukt melding geven aan de gebruiker
                                        Toast.makeText(MainActivity.this, "E-mailadres of wachtwoord is incorrect",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                    }
                }

            }
        }

    private void updateUI() {
        // alleen als emailadres admin@ is naar adminActivity
        if (emailadres.equals("admin@hostnet.nl")) {
            Toast.makeText(MainActivity.this, "Administrator ingelogd met e-mailadres " +
                    mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();

            // naar AdminActivity
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
        }

        else {
//            Toast.makeText(MainActivity.this, "Gebruiker ingelogd met e-mailadres " +
//                    mAuth.getCurrentUser().getEmail(), Toast.LENGTH_LONG).show();


            // naar ZoekActivity
            Intent intent = new Intent(MainActivity.this, ZoekActivity.class);
            startActivity(intent);
        }
    }

    public void naarRegistreren(View view) {
        Intent intent = new Intent(MainActivity.this, RegistreerActivity.class);
        startActivity(intent);
    }
}