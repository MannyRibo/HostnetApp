package com.example.hostnetapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickInloggen(View view) {
        Intent intent = new Intent(MainActivity.this, InlogschermActivity.class);
        startActivity(intent);
    }

    public void onClickWachtwoordReset(View view) {
        Intent intent = new Intent(MainActivity.this, WachtwoordVergetenActivity.class);
        startActivity(intent);
    }

}