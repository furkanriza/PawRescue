package com.example.pawrescue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;



    public  class Activity2 extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
            setContentView(R.layout.activity_2);

            BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
            bottomNav.setOnNavigationItemSelectedListener(navListener);

            // Başlangıçta ilk fragmenti yükle
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomePage(getApplicationContext())).commit();
        }

        private BottomNavigationView.OnNavigationItemSelectedListener navListener =
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        int id = item.getItemId();

                        if (id == R.id.nav_home) {
                            selectedFragment = new HomePage(getApplicationContext());
                        } else if (id == R.id.nav_animal_list) {
                            selectedFragment = new AnimalListPage();
                        } else if (id == R.id.nav_donation) {
                            selectedFragment = new DonationPage();
                        } else if (id == R.id.nav_rank_page) {
                            selectedFragment = new RankPage();
                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                selectedFragment).commit();

                        return true;
                    }
                };
    }
