package com.example.hostnetapp.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hostnetapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class ZoekActivity extends AppCompatActivity {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
    public static final String NAAM = "naam";
    public static final String IMAGEURL = "imageurl";
    private Context mContext;

    private EditText searchNameEdit;
    private ImageView profileImageZoekscherm;
    private Spinner searchAfdelingSpinner;
    private TextView profielVan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoek);

        searchNameEdit = findViewById(R.id.search_name_edit);
        searchAfdelingSpinner = findViewById(R.id.spinner_zoek);
        profileImageZoekscherm = findViewById(R.id.profile_view_edit);
        profielVan = findViewById(R.id.profielVan);

        searchAfdelingSpinner.setSelection(0);

        addItemsOnSpinner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(ZoekActivity.this, "Er ging iets mis met ophalen van data - " +
                                    e.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {

                    profielVan.setText(getString(R.string.ingelogdals, documentSnapshot.getString(NAAM)));
                    Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/schoolapp-97dd0.appspot.com/o/uploads%2F" + documentSnapshot.getString(IMAGEURL)).into(profileImageZoekscherm);


                }
            }
        });
    }

    public void onClickEditProfile(View view) {
        Intent intent = new Intent(ZoekActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    public void zoekOpNaam(View view) {
        String searchName = searchNameEdit.getText().toString();
        Intent intent = new Intent(ZoekActivity.this, ResultsActivity.class);
        intent.putExtra("searchname", searchName);
        startActivity(intent);
    }

    public void zoekOpAfdeling(View view) {
//        Toast.makeText(this, searchAfdelingSpinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        String searchAfdeling = searchAfdelingSpinner.getSelectedItem().toString();
        Intent intent = new Intent(ZoekActivity.this, ResultsActivity.class);
        intent.putExtra("searchafdeling", searchAfdeling);
        startActivity(intent);
    }

    public void naarRooster(View view) {
        Intent intent = new Intent(ZoekActivity.this, RoosterActivity.class);
        startActivity(intent);
    }

    public void addItemsOnSpinner() {
        searchAfdelingSpinner = (Spinner) findViewById(R.id.spinner_zoek);

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
        searchAfdelingSpinner.setAdapter(dataAdapter);

    }


}
