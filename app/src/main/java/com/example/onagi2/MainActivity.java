package com.example.onagi2;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    TextView txtName;
    TextView txtAbout;
    ImageView txtProfilePic;
    TextView toolbarTitle;
    ImageView leftIcon;
    BottomNavigationView bottomNavigationView;





    @Overrides
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.notify);
        badgeDrawable.setVisible(true);
        badgeDrawable.setNumber(4);



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        replaceFragment(new HomeFragment());
                        Toast.makeText(MainActivity.this,"Home",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.friends:
                        replaceFragment(new FriendsFragment());
                        Toast.makeText(MainActivity.this,"Friends!",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.profile:
                        replaceFragment(new ProfileFragment());
                        Toast.makeText(MainActivity.this,"Profile",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.notify:
                        replaceFragment(new NotifyFragment());
                        Toast.makeText(MainActivity.this,"Notifications",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.messages:
                        replaceFragment(new MessagesFragment());
                        Toast.makeText(MainActivity.this,"Messages",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
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