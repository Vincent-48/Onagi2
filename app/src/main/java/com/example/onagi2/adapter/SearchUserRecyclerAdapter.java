package com.example.onagi2.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onagi2.AndroidUtil;
import com.example.onagi2.FirebaseUtil;
import com.example.onagi2.MessageActivity;
import com.example.onagi2.Model.UserModel;
import com.example.onagi2.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclerAdapter.UserModelViewHolder> {

    Context context;
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options, Context context) {
        super(options);
        this.context =context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserModel model) {
        holder.usernameText.setText(model.getUsername());
        holder.phoneText.setText(model.getPhone());
        if (model.getUserId().equals(FirebaseUtil.currentUserId())){
            holder.usernameText.setText(model.getUsername()+"(Me)");
        }

        FirebaseUtil.getOtherProfilePicStorageRef(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(context,uri,holder.profilepic);
                    }

                });

        holder.itemView.setOnClickListener(v -> {
            //Navigate to Messages Activity
            Intent intent = new Intent(context, MessageActivity.class);
            AndroidUtil.passUserModelIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        });

    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView phoneText;
        ImageView profilepic;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilepic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }
}
