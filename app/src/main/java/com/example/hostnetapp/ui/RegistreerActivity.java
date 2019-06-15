package com.example.hostnetapp.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hostnetapp.R;
import com.example.hostnetapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistreerActivity extends AppCompatActivity {

    private EditText mRegistreerEmailadres;
    private EditText mRegistreerWachtwoord;
    private EditText mRegistreerNaam;
    private FirebaseAuth mAuth;
    private String naam;
    private String emailadres;
    private String wachtwoord;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference userRef;
    private static final String NAAM = "naam";
    private static final String EMAILADRES = "emailadres";
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registreer);

        mRegistreerNaam = findViewById(R.id.registreerNaam);
        mRegistreerEmailadres = findViewById(R.id.registreerEmailadres);
        mRegistreerWachtwoord = findViewById(R.id.registreerWachtwoord);

        mAuth = FirebaseAuth.getInstance();
    }

    public void updateUI(FirebaseUser user) {
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
if (documentSnapshot.exists()) {
userName = documentSnapshot.getString(NAAM);

} else {
    Toast.makeText(RegistreerActivity.this, "Dit document bestaat niet", Toast.LENGTH_LONG).show();
}
            }
        });
        Toast.makeText(RegistreerActivity.this, "Gebruiker geregistreerd met emailadres " +
                user.getEmail() + " en naam " + userName, Toast.LENGTH_LONG).show();
    }

    public void onClickRegistreren(View view) {
        naam = mRegistreerNaam.getText().toString();
        emailadres = mRegistreerEmailadres.getText().toString();
        wachtwoord = mRegistreerWachtwoord.getText().toString();

        // als emailadres of wachtwoord niet is ingevoerd toast weergeven
        if ((TextUtils.isEmpty(emailadres)) || (TextUtils.isEmpty(wachtwoord))
        || (TextUtils.isEmpty(naam))) {
            Toast.makeText(RegistreerActivity.this,
                    "Voer een naam, e-mailadres en wachtwoord in", Toast.LENGTH_LONG).show();
        }
        else if ((!TextUtils.isEmpty(emailadres)) || (!TextUtils.isEmpty(wachtwoord))
                || (!TextUtils.isEmpty(naam))) {
            if (!emailadres.contains("@")) {
                Toast.makeText(RegistreerActivity.this,
                        "Voer een geldig e-mailadres in", Toast.LENGTH_LONG).show();
            }

            if (emailadres.contains("@")) {

                int indexApenstaartje = emailadres.indexOf('@');

                String gedeelteNaApenstaartje = emailadres.substring(indexApenstaartje);

                // alleen als het emailadres eindigt op hostnet.nl verder gaan met registreren
                if (!gedeelteNaApenstaartje.equals("@hostnet.nl")) {
                    Toast.makeText(RegistreerActivity.this,
                            "Voer een e-mailadres in dat eindigt op @hostnet.nl", Toast.LENGTH_LONG).show();
                }
                if (gedeelteNaApenstaartje.equals("@hostnet.nl")) {
                    // als emailadres eindigt op @hostnet.nl gebruiker registreren
                    mAuth.createUserWithEmailAndPassword(emailadres, wachtwoord)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // nieuwe gebruiker aanmaken
                                        User user = new User(naam, emailadres);
                                        FirebaseDatabase.getInstance().getReference("Users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(user);

                                        Map<String, Object> newUser = new HashMap<>();
                                        newUser.put(NAAM, naam);
                                        newUser.put(EMAILADRES, emailadres);
                                        db.collection("Users").document(
                                                FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .set(newUser);

                                        userRef = db.collection("Users").document(FirebaseAuth.getInstance()
                                                .getCurrentUser().getUid());
                                        userRef.set(newUser);

                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        updateUI(currentUser);

                                    }
                                    else {
                                        Toast.makeText(RegistreerActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        }
    }
}