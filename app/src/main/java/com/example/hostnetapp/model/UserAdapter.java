package com.example.hostnetapp.model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hostnetapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.UserHolder> {

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User model) {
        holder.textViewTitle.setText(model.getNaam());
        holder.imageView.setImageResource(R.drawable.profile2);
//        holder.textViewDescription.setText(model.getEmailadres());
//        holder.textViewPriority.setText("1");
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_cardview,
                parent, false);
        return new UserHolder(v);
    }

    class UserHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageView;

        public UserHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            imageView = itemView.findViewById(R.id.imageView_image);
//            textViewDescription = itemView.findViewById(R.id.text_view_description);
//            textViewPriority = itemView.findViewById(R.id.text_view_priority);
        }
    }
}
