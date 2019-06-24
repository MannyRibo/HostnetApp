package com.example.hostnetapp.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.hostnetapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.ViewHolder> {

    private OnItemClickListener listener;
    private Context mContext;

    public UserAdapter(Context mContext, @NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
        this.mContext = mContext;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull User user) {
        holder.textViewTitle.setText(user.getNaam());
        String url = user.getImageurl();
        System.out.println("liewe "+url);
        Glide.with(mContext).load("https://firebasestorage.googleapis.com/v0/b/schoolapp-97dd0.appspot.com/o/uploads%2F"+url).into(holder.imageView);

//        if (user.getImageurl() == null) {
//            holder.imageView.setImageResource(R.drawable.profilepicture);
//            holder.imageView.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.imageView.setImageDrawable(Drawable.createFromPath(url));
//            holder.imageView.setVisibility(View.VISIBLE);
//        }


//        holder.imageView.setImageResource(R.drawable.profile2);
//        holder.textViewDescription.setText(user.getEmailadres());
//        holder.textViewPriority.setText("1");
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_user_cardview,
                parent, false);
        return new ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            imageView = itemView.findViewById(R.id.profileImage_cardview);

//            itemView.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {

                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }else{
                        System.out.println(position);
                        System.out.println(getSnapshots().getSnapshot(position).getId());
                    }

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
