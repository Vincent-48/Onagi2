package com.example.onagi2;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onagi2.Model.ChatMessageModel;
import com.example.onagi2.Model.ChatroomModel;
import com.example.onagi2.Model.UserModel;
import com.example.onagi2.adapter.ChatRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

import org.json.JSONObject;

import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MessageActivity extends AppCompatActivity {

    String chatroomId;
    ChatroomModel chatroomModel;
    ChatRecyclerAdapter adapter;
    UserModel otherUser;
    EditText messageInput;
    ImageButton sendMessageBtn;
    ImageButton backBtn;
    TextView otherUsername;
    RecyclerView recyclerView;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //get UserModel
        otherUser = AndroidUtil.getUserModelFromIntent(getIntent());
        chatroomId = FirebaseUtil.getChatroomId(FirebaseUtil.currentUserId(), otherUser.getUserId());

        messageInput = findViewById(R.id.chat_message_input);
        backBtn = findViewById(R.id.back_btn);
        otherUsername = findViewById(R.id.other_username);
        sendMessageBtn = findViewById(R.id.message_send_btn);
        recyclerView = findViewById(R.id.chat_recycler_view);
        imageView = findViewById(R.id.profile_pic_image_view);

        FirebaseUtil.getOtherProfilePicStorageRef(otherUser.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if (t.isSuccessful()){
                        Uri uri = t.getResult();
                        AndroidUtil.setProfilePic(this,uri,imageView);
                    }

                });

        backBtn.setOnClickListener((v)->{
            onBackPressed();
        });
        sendMessageBtn.setOnClickListener(v -> {
            String message = messageInput.getText().toString().trim();
            if (message.isEmpty()){
                return;
            }
            else {
                sendMessageToUser(message);
            }

        });

       otherUsername.setText(otherUser.getUsername());
       getOrCreateChatroomModel();
       setupChatRecyclerView();

    }
    void setupChatRecyclerView(){
        Query query = FirebaseUtil.getChatroomMessageReference(chatroomId)
                .orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<ChatMessageModel> options = new FirestoreRecyclerOptions.Builder<ChatMessageModel>()
                .setQuery(query,ChatMessageModel.class).build();

        adapter = new ChatRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }
    void sendMessageToUser(String message){
        chatroomModel.setLastMessageTimestamp(Timestamp.now());
        chatroomModel.setLastMessageSenderId(FirebaseUtil.currentUserId());
        chatroomModel.setLastMessage(message);

        FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel);

        ChatMessageModel chatMessageModel = new ChatMessageModel(message,FirebaseUtil.currentUserId(),Timestamp.now());
        FirebaseUtil.getChatroomMessageReference(chatroomId).add(chatMessageModel)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            messageInput.setText("");
                            sendNotification(message);
                        }
                    }
                });

    }
    void getOrCreateChatroomModel(){
        FirebaseUtil.getChatroomReference(chatroomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                chatroomModel = task.getResult().toObject(ChatroomModel.class);
                if(chatroomModel==null){
                    //first time chat
                    chatroomModel = new ChatroomModel(
                            chatroomId,
                            Arrays.asList(FirebaseUtil.currentUserId(),otherUser.getUserId()),
                            Timestamp.now(),
                            ""
                    );
                    FirebaseUtil.getChatroomReference(chatroomId).set(chatroomModel)
                            .addOnCompleteListener(setTask -> {
                                if (setTask.isSuccessful()) {
                                    // Perform actions dependent on the chatroomModel here
                                } else {
                                    // Handle failure to create the chatroom
                                }
                            });



            }
        }
     });
    }
    void sendNotification(String message){
        // current username, message, currentuserid, otheruser token
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               UserModel currentUser = task.getResult().toObject(UserModel.class);
               try {
                   JSONObject jsonObject = new JSONObject();

                   JSONObject notificationObj = new JSONObject();
                   notificationObj.put("title",currentUser.getUsername());
                   notificationObj.put("body",message);

                   JSONObject dataObj = new JSONObject();
                   dataObj.put("userId",currentUser.getUserId());

                   jsonObject.put("notification", notificationObj);
                   jsonObject.put("data", dataObj);
                   jsonObject.put("to", otherUser.getFcmToken());

                   callApi(jsonObject);
               }
               catch (Exception e){

               }
           }
        });

    }
    void callApi(JSONObject jsonObject){
        MediaType JSON = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization","Bearer AAAAZ6CFRB8:APA91bHZWSPaPiPg4oIATogiY3czokjqqXiFfcEnr834MPhYMpnGnC9PsfjiAgQUoFWPErEgJdy-owjuBSLBRYHa88kGpu58RHvFCsKgu2R90qJgAXe5f2pnvA8kGtbWrXzzTvzI8s-T")
                .build();
        client.newCall(request);
    }





}