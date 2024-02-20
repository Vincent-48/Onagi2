package com.example.onagi2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.onagi2.Model.UserModel;
import com.github.dhaval2404.imagepicker.ImagePicker;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class ProfileFragment extends Fragment {

    ImageView profilepic;
    EditText usernameInput;
    EditText phoneInput;
    Button updateprofileBtn;
    ProgressBar progressBar;
    TextView logoutBtn;
    UserModel currentUserModel;
    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;

    View view;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result ->{
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data !=null && data.getData() != null){
                            selectedImageUri = data.getData();
                            AndroidUtil.setProfilePic(getContext(),selectedImageUri,profilepic);

                        }
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profilepic = view.findViewById(R.id.imageprofile);
        usernameInput = view.findViewById(R.id.profile_username);
        phoneInput = view.findViewById(R.id.profile_phone);
        updateprofileBtn = view.findViewById(R.id.profile_update_btn);
        progressBar = view.findViewById(R.id.profile_progress_bar);
        logoutBtn = view.findViewById(R.id.log_out_btn);

        getUserData();
        updateprofileBtn.setOnClickListener(v -> {
            updatebtnClick();
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtil.logout();
                Intent intent = new Intent(getContext(),SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        profilepic.setOnClickListener(v -> {
            ImagePicker.with(this).cropSquare().compress(512).maxResultSize(512,512)
                    .createIntent(new Function1<Intent, Unit>() {
                        @Override
                        public Unit invoke(Intent intent) {
                            imagePickLauncher.launch(intent);
                            return null;
                        }
                    });
        });

        return view;

    }
    void updatebtnClick(){
        String newUsername = usernameInput.getText().toString();
        if (newUsername.isEmpty() || newUsername.length()<3){
            usernameInput.setError("Username should be at least 3chars");
            return;
        }
        currentUserModel.setUsername(newUsername);
        setInProgress(true);

        if (selectedImageUri != null){
            FirebaseUtil.getCurrentProfilePicStorageRef().putFile(selectedImageUri)
                    .addOnCompleteListener(task -> {
                       updateToFirestore();
                    });
        }
        updateToFirestore();
    }
    void updateToFirestore(){
        FirebaseUtil.currentUserDetails().set(currentUserModel)
                .addOnCompleteListener(task -> {
                    setInProgress(false);
                   if (task.isSuccessful()){
                       AndroidUtil.showToast(getContext(),"Updated Successfully");
                   }
                   else
                       AndroidUtil.showToast(getContext(),"Update failed");
                });
    }
    void getUserData(){
        FirebaseUtil.getCurrentProfilePicStorageRef().getDownloadUrl()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                Uri uri = task.getResult();
                                AndroidUtil.setProfilePic(getContext(),uri,profilepic);
                            }

                        });
        setInProgress(true);
        FirebaseUtil.currentUserDetails().get().addOnCompleteListener(task -> {
            setInProgress(false);
            currentUserModel = task.getResult().toObject(UserModel.class);
            usernameInput.setText(currentUserModel.getUsername());
            phoneInput.setText(currentUserModel.getPhone());
        });
    }
    void setInProgress(boolean inProgress){

            if(inProgress){
                progressBar.setVisibility(View.VISIBLE);
                updateprofileBtn.setVisibility(View.GONE);
            }
            else {
                progressBar.setVisibility(View.GONE);
                updateprofileBtn.setVisibility(View.VISIBLE);
            }

    }
}