package com.example.onagi2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.onagi2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    TextView txtName;
    TextView txtAbout;
    ImageView txtProfilePic;
    ActivityMainBinding binding;




    @Overrides
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    replaceFragment(new HomeFragment());
                    Toast.makeText(this,"Home section",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.profile:
                    replaceFragment(new ProfileFragment());
                    Toast.makeText(this,"Profile section",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.messages:
                    replaceFragment(new MessageFragment());
                    Toast.makeText(this,"Messages section",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.notify:
                    replaceFragment(new NotifyFragment());
                    Toast.makeText(this,"Notifications section",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.friends:
                    replaceFragment(new FriendsFragment());
                    Toast.makeText(this,"Friends section",Toast.LENGTH_SHORT).show();

                    break;
            }
            return true;
        });


        txtProfilePic = findViewById(R.id.imageView);

        ImageView leftIcon = findViewById(R.id.left_icon);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        ImageView rightIcon = findViewById(R.id.right_icon);

        leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"back button clicked",Toast.LENGTH_SHORT).show();
            }
        });
        rightIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(v);
            }
            private void showMenu(View v){
                PopupMenu popupMenu = new PopupMenu(MainActivity.this,v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.one)
                            Toast.makeText(MainActivity.this,"First menu clicked",Toast.LENGTH_SHORT).show();
                        if (item.getItemId() == R.id.two)
                            Toast.makeText(MainActivity.this,"Second menu clicked",Toast.LENGTH_SHORT).show();
                        if (item.getItemId() == R.id.three)
                            Toast.makeText(MainActivity.this,"Third menu clicked",Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
                popupMenu.show();
            }
        });









    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
}