package com.example.onagi2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginOtpActivity extends AppCompatActivity {

    String phoneNumber;

    EditText inputOtp;
    Button nextBtn;
    ProgressBar progressBar;
    TextView resendOtpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        inputOtp = findViewById(R.id.login_otp);
        nextBtn = findViewById(R.id.login_next_btn);
        progressBar = findViewById(R.id.login_progress_bar);
        resendOtpTextView = findViewById(R.id.resend_otp_textview);

        phoneNumber = getIntent().getExtras().getString("phone");
        Toast.makeText(getApplicationContext(),phoneNumber,Toast.LENGTH_SHORT).show();


    }
    void sendOtp(String phoneNumber, boolean isResend){

    }
    void setInProgress(boolean inProgress){
        if(inProgress)
    }
}