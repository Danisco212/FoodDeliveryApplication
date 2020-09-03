package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.myapplication.Fragments.HomeFragment;
import com.example.myapplication.Fragments.MonitorOrderFragment;
import com.example.myapplication.Fragments.OrderHistoryFragment;
import com.example.myapplication.Fragments.ProfileFragments;
import com.example.myapplication.Fragments.RestaurantsFragment;
import com.example.myapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private boolean atHome = true;
    private Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize(){
        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        setUpNavigationView();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        loadHomeFragment();
    }

    // setting up the slide menu click functions
    private void setUpNavigationView(){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        if (!atHome){
                            loadHomeFragment();
                        }
                        break;
                    case R.id.cart:
                        if (userLoggedIn()){
                            startActivity(new Intent(MainActivity.this, CartActivity.class));
                        }
                        break;
                    case R.id.history:
                        if (userLoggedIn()){
                            loadFragment(new OrderHistoryFragment());
                            setTitle("Order History");
                        }
                        break;
                    case R.id.monitor:
                        if (userLoggedIn()){
                            loadFragment(new MonitorOrderFragment());
                            setTitle("Monitor Order");
                        }
                        break;
                    case R.id.restaurants:
                        loadFragment(new RestaurantsFragment());
                        setTitle("Restaurants");
                        break;
                    case R.id.profile:
                        loadFragment(new ProfileFragments());
                        setTitle("My Profile");
                }
                return false;
            }
        });
    }

    // this is how the fragments are loaded, using fragment managers
    private void loadFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        // replace the fragment holder, with the fragment class
        transaction.replace(R.id.pagesHolder, fragment);
        drawerLayout.closeDrawers();
        transaction.commit();
        atHome = false;
    }

    // checking if user is logged in for some of the pages
    public boolean userLoggedIn(){
        if (mAuth.getCurrentUser()==null){
            drawerLayout.closeDrawers();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to be logged in")
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // start the login activity here
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog dialog = builder.create();
            dialog.show();
            return false;
        }else{
            return true;
        }
    }

    // this is the function to load the default fragment
    private void loadHomeFragment(){
        atHome = true;
        loadFragment(new HomeFragment());
        setTitle("Home");
    }

    // this is where we set the title of the pages
    private void setTitle(String title){
        toolbar.setTitle(title);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else if (!atHome){
            loadHomeFragment();
        }else{
            super.onBackPressed();
        }
    }
}