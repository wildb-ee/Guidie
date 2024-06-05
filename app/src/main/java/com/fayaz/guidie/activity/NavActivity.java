package com.fayaz.guidie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.fayaz.guidie.R;

import com.fayaz.guidie.fragment.CityFragment;
import com.fayaz.guidie.fragment.LocationFragment;
import com.fayaz.guidie.fragment.MainFragment;
import com.fayaz.guidie.fragment.PersonalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class NavActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_activity);
        bottomNavigationView = findViewById(R.id.bottom_navigation_id);
        frameLayout = findViewById(R.id.fragment_container);
        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment_container,new MainFragment()).addToBackStack
                        (null)
                .commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getTitle().toString()){
                    case "Home": fragment = new MainFragment(); break;
                    case "City": fragment = new CityFragment(); break;
                    case "Location": fragment = new LocationFragment();break;
                    case "Personal": fragment = new PersonalFragment();break;
                }
                if (fragment == null){
                    return false;
                }
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                return true;
            }
        });

    }


}
