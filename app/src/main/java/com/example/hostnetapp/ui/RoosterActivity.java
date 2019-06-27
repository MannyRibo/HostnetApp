package com.example.hostnetapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class RoosterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef;
    private TextView roosterVan, maandag, dinsdag, woensdag, donderdag, vrijdag, zaterdag, zondag;
    private String naamVanGebruiker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooster);

        userRef = db.collection("Users").document(mAuth.getCurrentUser().getUid());

        roosterVan = findViewById(R.id.roosterVanTitel);
        maandag = findViewById(R.id.roosterTijdMaandag);
        dinsdag = findViewById(R.id.roosterTijdDinsdag);
        woensdag = findViewById(R.id.roosterTijdWoensdag);
        donderdag = findViewById(R.id.roosterTijdDonderdag);
        vrijdag = findViewById(R.id.roosterTijdVrijdag);
        zaterdag = findViewById(R.id.roosterTijdZaterdag);
        zondag = findViewById(R.id.roosterTijdZondag);

    }

    @Override
    protected void onStart() {
        super.onStart();
        userRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(RoosterActivity.this, "Er ging iets mis met ophalen van data - " +
                                    e.toString(),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);

                    naamVanGebruiker = user.getNaam();

                    roosterVan.setText(getString(R.string.roostervan, user.getNaam()));
                    maandag.setText(user.getRooster().getMaandag());
                    dinsdag.setText(user.getRooster().getDinsdag());
                    woensdag.setText(user.getRooster().getWoensdag());
                    donderdag.setText(user.getRooster().getDonderdag());
                    vrijdag.setText(user.getRooster().getVrijdag());
                    zaterdag.setText(user.getRooster().getZaterdag());
                    zondag.setText(user.getRooster().getZondag());
                }
            }
        });
    }

    /**
     * @param view is de view waarop geklikt dient te worden om de methode te triggeren
     *             Deze methode zorgt ervoor dat er een intent wordt gemaakt
     *             met de roostergegevens van de gebruiker, zodat deze zijn rooster kan delen
     *             via social media
     */
    public void roosterDelen(View view) {

        String roosterAlsTekst = "Hey, mijn rooster is: \n" + "\nMaandag: " +  maandag.getText().toString() +
                "\nDinsdag: " +  dinsdag.getText().toString() +
                "\nWoensdag: " +  woensdag.getText().toString() +
                "\nDonderdag: " +  donderdag.getText().toString() +
                "\nVrijdag: " +  vrijdag.getText().toString() +
                "\nZaterdag: " +  zaterdag.getText().toString() +
                "\nZondag: " +  zondag.getText().toString()
                + "\n\n"
                + "Groetjes van " + naamVanGebruiker;

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, roosterAlsTekst);
        intent.setType("text/plain");
        startActivity(intent);
    }

}
