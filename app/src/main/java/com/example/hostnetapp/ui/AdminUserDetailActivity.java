package com.example.hostnetapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentUserEmail = mAuth.getCurrentUser().getEmail();
    private DocumentReference userRef;

    public static final String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (currentUserEmail.equals("admin@hostnet.nl")){
            setContentView(R.layout.user_details);
        }else{
            setContentView(R.layout.result_user_details);
        }

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
                    Glide.with(getApplicationContext()).load("https://firebasestorage.googleapis.com/v0/b/schoolapp-97dd0.appspot.com/o/uploads%2F" + documentSnapshot.getString(IMAGEURL)).into(profielfoto);

                    User newUser = (documentSnapshot.toObject(User.class));

                    maandag.setText(newUser.getRooster().getMaandag());
                    dinsdag.setText(newUser.getRooster().getDinsdag());
                    woensdag.setText(newUser.getRooster().getWoensdag());
                    donderdag.setText(newUser.getRooster().getDonderdag());
                    vrijdag.setText(newUser.getRooster().getVrijdag());
                    zaterdag.setText(newUser.getRooster().getZaterdag());
                    zondag.setText(newUser.getRooster().getZondag());
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
