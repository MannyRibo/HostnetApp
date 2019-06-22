package com.example.hostnetapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.hostnetapp.R;
import com.example.hostnetapp.model.Rooster;
import com.example.hostnetapp.model.User;
import com.example.hostnetapp.model.UserAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminResultsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("Users");
    private UserAdapter adapter;
    public static final String USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

            setTitle("Alle werknemers");
            Query query = userRef.orderBy("naam").startAt("Administratie");
//             = userRef.orderBy("name").startAt(searchName).endAt(searchName + "\uf8ff");
//            Query query = FirebaseFirestore.getInstance().collection("Users").whereGreaterThanOrEqualTo("naam", searchName);
//            Query query = db.collection("Users").startAt(searchAfdeling+ "\uf8ff");
            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                    .setQuery(query, User.class)
                    .build();

            adapter = new UserAdapter(this, options);

            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new UserAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    User user = documentSnapshot.toObject(User.class);

                    Intent intent = new Intent(AdminResultsActivity.this, AdminUserDetailActivity.class);
                    intent.putExtra(USER, user);
                    startActivity(intent);

                }
            });
        }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}