package com.example.onagi2;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {
    public static String currentUserId(){
        return FirebaseAuth.getInstance().getUid();

    }
    public static boolean isLoggedIn(){
        if (currentUserId() != null){
            return true;
        }
        else {
            return false;
        }
    }
    public static String currentUserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }
    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }
    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }
    public static String getChatroomId(String userId1, String userId2){
        if (userId1.hashCode() < userId2.hashCode()) {
            return userId1 + " " + userId2;
        }
        else {
            return userId2 + " " + userId1;
        }
    }
}
