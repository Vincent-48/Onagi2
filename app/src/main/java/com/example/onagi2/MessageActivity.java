package com.example.onagi2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onagi2.Model.ChatroomModel;
import com.example.onagi2.Model.UserModel;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    String chatroomId;
    ChatroomModel chatroomModel;
    UserModel otherUser;
    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherUsername;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //get UserModel
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(),otherUser.getUserId());

        messageInput = findViewById(R.id.chat_message_input);
        backBtn = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        recyclerView = findViewById(R.id.chat_recycler_view);

       backBtn.setOnClickListener(v -> {
           onBackPressed();
       });

       otherUsername.setText(otherUser.getUsername());
       getOrCreateChatroomModel();

    }
    void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                chatroomModel = task.getResult().toObject(ChatroomModel.class);
                if (chatroomModel == null){
                    //first time chat
                    List<String> userIds = new ArrayList<>();
                    userIds.add(FirebaseUtil.currentUserDetails());
                    userIds.add(otherUser.getUserId());

                    chatroomModel = new ChatroomModel(
                            chatroomId,
                            userIds,
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, SearchUserActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(backIntent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView.setAdapter(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        recyclerView.setAdapter(null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerView.setAdapter(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
    }
}