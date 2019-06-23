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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ResultsActivity extends AppCompatActivity  {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("Users");
    private UserAdapter adapter;
    public static final String USER = "user";
//    private OnItemClickListener mItemClickListener;

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

        if (searchName == null) {
            setTitle("Resultaten: "+ searchAfdeling);
            Query query = userRef.whereEqualTo("afdeling", searchAfdeling);

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

                    Intent intent = new Intent(ResultsActivity.this, AdminUserDetailActivity.class);
                    intent.putExtra(USER, user);
                    startActivity(intent);

                }
            });
        } else {
            setTitle("Resultaten: "+ searchName);
            Query query = userRef.whereEqualTo("naam", searchName);
//            String str1 = document.getString("yourProperty");
//            String str2 = "Cali";
//            boolean b = str1.toLowerCase().contains(str2.toLowerCase());

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

                    Intent intent = new Intent(ResultsActivity.this, AdminUserDetailActivity.class);
                    intent.putExtra(USER, user);
                    startActivity(intent);

                }
            });
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


//    @Override
//    public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
////        TupperMeal tupperMeal = mTupperMeals.get(position);
////        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
////        startActivity(intent);
////        Toast.makeText(this, tupperMeal.toString(), Toast.LENGTH_SHORT).show();
//        System.out.println(position);
////        Intent intent = new Intent(MainActivity.this, AddEditActivity.class);
////        intent.putExtra(MainActivity.EXTRA_TUPPERMEAL, tupperMeal);
////        startActivity(intent);
//    }
}