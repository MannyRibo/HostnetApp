package com.example.hostnetapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.hostnetapp.R;
import com.example.hostnetapp.model.User;
import com.example.hostnetapp.model.UserAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ResultsActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("Users");
    private UserAdapter adapter;

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
        Intent intent = getIntent();
        String searchName = intent.getStringExtra("searchname");
        String searchAfdeling = intent.getStringExtra("searchafdeling");
//intent.getStringExtra("seachname")
        if (searchName == null) {
            setTitle("Resultaten: "+ searchAfdeling);
            Query query = userRef.whereEqualTo("afdeling", searchAfdeling);

            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                    .setQuery(query, User.class)
                    .build();

            adapter = new UserAdapter(options);

            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            setTitle("Resultaten: "+ searchName);
            Query query = userRef.whereEqualTo("naam", searchName);
//            String str1 = document.getString("yourProperty");
//            String str2 = "Cali";
//            boolean b = str1.toLowerCase().contains(str2.toLowerCase());
//             = userRef.orderBy("name").startAt(searchName).endAt(searchName + "\uf8ff");
//            Query query = FirebaseFirestore.getInstance().collection("Users").whereGreaterThanOrEqualTo("naam", searchName);
//            Query query = db.collection("Users").startAt(searchAfdeling+ "\uf8ff");
            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                    .setQuery(query, User.class)
                    .build();

            adapter = new UserAdapter(options);

            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        }
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