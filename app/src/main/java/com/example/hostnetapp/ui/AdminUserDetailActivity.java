package com.example.hostnetapp.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hostnetapp.R;
import com.example.hostnetapp.model.User;
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
                    if (documentSnapshot.getString(IMAGEURL) == null) {
                        profielfoto.setImageResource(R.drawable.profilepicture);
                        profielfoto.setVisibility(View.VISIBLE);
                    } else {
                        profielfoto.setImageDrawable(Drawable.createFromPath(documentSnapshot.getString(IMAGEURL)));
                        profielfoto.setVisibility(View.VISIBLE);
                    }
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
