package com.example.hostnetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WachtwoordVergetenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wachtwoord_vergeten);
    }

    public void onClickWachtwoordGereset(View view) {
        //make toast wachtwoord gereset
    }
}
