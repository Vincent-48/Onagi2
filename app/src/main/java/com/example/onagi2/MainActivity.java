package com.example.onagi2;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {
    TextView txtName;
    TextView txtAbout;
    ImageView txtProfilePic;
    TextView toolbarTitle;
    ImageView leftIcon;
    BottomNavigationView bottomNavigationView;
    ImageButton searchButton;





    @Overrides
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        searchButton = findViewById(R.id.main_search_btn);

        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.notify);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(4);

        searchButton.setOnClickListener((v ->
                startActivity(new Intent(MainActivity.this, SearchUserActivity.class))));

        getFCMToken();



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        replaceFragment(new HomeFragment());

                        break;
                    case R.id.friends:
                        replaceFragment(new FriendsFragment());
                        break;
                    case R.id.profile:
                        replaceFragment(new ProfileFragment());
                        break;
                    case R.id.notify:
                        replaceFragment(new NotifyFragment());
                        break;
                    case R.id.messages:
                        replaceFragment(new MessagesFragment());
                        break;
                }
                return true;
            }
        });






    }
    void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                String token = task.getResult();
                FirebaseUtil.currentUserDetails().update("fcmToken",token);
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
}