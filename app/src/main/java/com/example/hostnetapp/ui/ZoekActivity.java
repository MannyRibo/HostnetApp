package com.example.hostnetapp.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostnetapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class ZoekActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());
    public static final String NAAM = "naam";

    private TextView naam;
    private EditText searchNameEdit;
    private Spinner searchAfdelingSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoek);

        searchNameEdit = findViewById(R.id.search_name_edit);
        searchAfdelingSpinner = findViewById(R.id.spinner_zoek);
        naam = findViewById(R.id.naamMedewerker);
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
                    naam.setText(documentSnapshot.getString(NAAM));
                    naam.setVisibility(View.VISIBLE);
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
        List<String> list = new ArrayList<String>();
        list.add("Kies je afdeling...");
        list.add("Administratie");
        list.add("Directie");
        list.add("Operations");
        list.add("Personeelszaken");
        list.add("Software Engineering");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list) {
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
