package com.example.hostnetapp.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.hostnetapp.R;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    public void onClickSaveProfile(View view) {
        Intent intent = new Intent(EditProfileActivity.this, ZoekActivity.class);
        startActivity(intent);
    }
}
