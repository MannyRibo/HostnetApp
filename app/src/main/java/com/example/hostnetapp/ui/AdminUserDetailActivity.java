package com.example.hostnetapp.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostnetapp.R;
import com.example.hostnetapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class AdminUserDetailActivity extends AppCompatActivity {

    private TextView naam, afdeling, emailadres, telefoonnummer, maandag,
    dinsdag, woensdag, donderdag, vrijdag, zaterdag, zondag, profielNaam;

    private ImageView profielfoto;

    private static final String IMAGEURL = "imageurl";

    User user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef;

    public static final String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);

        user = getIntent().getParcelableExtra(AdminResultsActivity.USER);

        userRef = db.collection("Users").document(user.getUserID());

        naam = findViewById(R.id.naam_details_text);
        afdeling = findViewById(R.id.afdeling_details_text);
        emailadres = findViewById(R.id.email_details_text);
        telefoonnummer = findViewById(R.id.telefoonnummer_details_text);
        maandag = findViewById(R.id.tijdMaandag);
        dinsdag = findViewById(R.id.tijdDinsdag);
        woensdag = findViewById(R.id.tijdWoensdag);
        donderdag = findViewById(R.id.tijdDonderdag);
        vrijdag = findViewById(R.id.tijdVrijdag);
        zaterdag = findViewById(R.id.tijdZaterdag);
        zondag = findViewById(R.id.tijdZondag);
        profielNaam = findViewById(R.id.profielVan_details);
        profielfoto = findViewById(R.id.profielfoto_details);

        profielNaam.setText(getString(R.string.profielvan, user.getNaam()));
        naam.setText(user.getNaam());
        afdeling.setText(user.getAfdeling());
        emailadres.setText(user.getEmailadres());
        telefoonnummer.setText(user.getTelefoonnummer());
        maandag.setText(user.getRooster().getMaandag());
        dinsdag.setText(user.getRooster().getDinsdag());
        woensdag.setText(user.getRooster().getWoensdag());
        donderdag.setText(user.getRooster().getDonderdag());
        vrijdag.setText(user.getRooster().getVrijdag());
        zaterdag.setText(user.getRooster().getZaterdag());
        zondag.setText(user.getRooster().getZondag());
    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(AdminUserDetailActivity.this, "Er ging iets mis met ophalen van data - " +
                                    e.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {

                    profielfoto.setImageDrawable(Drawable.createFromPath(documentSnapshot.getString(IMAGEURL)));
                    profielfoto.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void editRooster(View view) {
        Intent intent = new Intent(AdminUserDetailActivity.this, AdminUserDetailEditActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
    }
}
