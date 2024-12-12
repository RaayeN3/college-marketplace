package com.example.trialapp;

import static com.example.trialapp.R.*;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
//    TextView textView;
    FirebaseUser user;
//    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment=new HomeFragment();
//    ChatFragment chatFragment=new ChatFragment();
    MyAdsFragment myAdsFragment =new MyAdsFragment();
    AccountFragment accountFragment=new AccountFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottomNavigationView);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,homeFragment).commit();
                        return true;
//                    case R.id.chat:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,chatFragment).commit();
//                        return true;
                    case R.id.myads:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, myAdsFragment).commit();
                        return true;
                    case R.id.account:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,accountFragment).commit();
                        return true;

                }

                return false;
            }
        });



    }
    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();

    }
}