package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Adapters.CategoryAdapter;
import com.example.myapplication.Entities.Product;
import com.example.myapplication.Entities.Restaurant;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String name;
    private DatabaseReference mDatabase;

    private Restaurant restaurant;
    private ImageView resPic;
    private TextView resName;
    private RecyclerView categoriesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        initialize();
    }

    private void initialize(){
        restaurant = new Restaurant();

        categoriesView = findViewById(R.id.categoriesView);
        resPic = findViewById(R.id.restaurant_image);
        resName = findViewById(R.id.restaurant_name);
        name = getIntent().getStringExtra("name");
        mDatabase = FirebaseDatabase.getInstance().getReference("restaurant").child(name);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getRestaurant();
    }

    // filling name and picture
    private void fillingRestaurantInformation(){
        resName.setText(restaurant.getName());
        Glide.with(this).load(restaurant.getImage()).into(resPic);
    }

    // filling up the categories
    private void fillUpcategories(){
        CategoryAdapter adapter = new CategoryAdapter(this);
        List<String> categoriesList = new ArrayList<>();
        for (String key: restaurant.getCategories().keySet()){
            categoriesList.add(key);
        }
        adapter.setCategories(categoriesList);
        adapter.setRestaurant(restaurant);
        categoriesView.setAdapter(adapter);
        categoriesView.setLayoutManager(new LinearLayoutManager(this));
    }

    // getting the categories of each the restaurant
    private void getRestaurant(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                    restaurant = snapshot.getValue(Restaurant.class);
                    fillingRestaurantInformation();
                    fillUpcategories();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}