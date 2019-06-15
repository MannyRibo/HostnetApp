package com.example.hostnetapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hostnetapp.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private EditText mEmailadres;
    private EditText mWachtwoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmailadres = findViewById(R.id.emailadres);
        mWachtwoord = findViewById(R.id.wachtwoord);

    }

    public void onClickInloggen(View view) {

        String emailadres = mEmailadres.getText().toString();
        String wachtwoord = mWachtwoord.getText().toString();

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

                    // alleen als emailadres admin@ is naar adminActivity
                        if (emailadres.equals("admin@hostnet.nl")) {
                            // inloggen in firebase en naar AdminActivity
                            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                            startActivity(intent);
                        }
                        // inloggen in firebase en naar ZoekActivity
                        else {
                            Intent intent = new Intent(MainActivity.this, ZoekActivity.class);
                            startActivity(intent);
                        }
                    }
                }

            }
        }

    public void onClickWachtwoordReset(View view) {
        // wachtwoordreset email sturen met firebase
    }

    public void naarRegistreren(View view) {
        Intent intent = new Intent(MainActivity.this, RegistreerActivity.class);
        startActivity(intent);
    }
}