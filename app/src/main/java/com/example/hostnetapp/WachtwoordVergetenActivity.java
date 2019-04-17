package com.example.hostnetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WachtwoordVergetenActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wachtwoord_vergeten);
    }

    public void onClickWachtwoordGereset(View view) {

        EditText emailInvoer = findViewById(R.id.emailInvoer);

        String emailadres = emailInvoer.getText().toString();

        if (TextUtils.isEmpty(emailadres)) {
            Toast.makeText(this, "Voer een e-mailadres in", Toast.LENGTH_LONG).show();
        }
        else
        Toast.makeText(this, "E-mail is gestuurd naar " + emailadres, Toast.LENGTH_LONG).show();
    }
}
