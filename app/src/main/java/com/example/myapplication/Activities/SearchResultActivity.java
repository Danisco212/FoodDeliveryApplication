package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.Adapters.HighlightedProductsAdapter;
import com.example.myapplication.Entities.Product;
import com.example.myapplication.Entities.Restaurant;
import com.example.myapplication.Fragments.HomeFragment;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView resultsView;
    private ProgressBar loading;
    private TextView noResults, search;

    private String searchTearm;

    private List<Product> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        initialize();

    }

    // initializing the UI
    private void initialize(){
        resultList = new ArrayList<>();
        searchTearm = getIntent().getStringExtra("search");

        loading = findViewById(R.id.loading);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(searchTearm);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        noResults = findViewById(R.id.noResults);
        search = findViewById(R.id.search_term);
        search.setText("Search results for: \n"+searchTearm);
        resultsView = findViewById(R.id.results);

        searchLogic();
    }

    // this handles the search logic, goes through all of the products and gives the result that matches the search
    private void searchLogic(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("restaurant");

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    // get all the restaurants from the database
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    // search the products of each restaurant, if it matches, add it to the result list
                    // all the categories in the restaurant
                    for (String category: restaurant.getCategories().keySet()){
                        // all the products of each category
                        for (Product product: restaurant.getCategories().get(category)){
                            // compare and add if matches
                            if (product!=null){
                                if (product.getName().toLowerCase().contains(searchTearm)){
                                    resultList.add(product);
                                }
                            }
                        }
                    }
                    // when finished adding the products, if the result is more than 0, then show it, if not, show error
                    if (resultList.size()>0){
                        showResults();
                    }else{
                        loading.setVisibility(View.GONE);
                        noResults.setVisibility(View.VISIBLE);
                    }
                    Log.e("Key", ""+resultList.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // filling the results recycler view
    private void showResults(){
        loading.setVisibility(View.GONE);
        noResults.setVisibility(View.GONE);
        HighlightedProductsAdapter adapter = new HighlightedProductsAdapter(this,resultList);
        resultsView.setAdapter(adapter);
        adapter.setOnItemClickListener(new HighlightedProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("name", resultList.get(position).getName());
                bundle.putString("desc", resultList.get(position).getDescription());
                bundle.putString("image", resultList.get(position).getImage());
                bundle.putString("restaurant", resultList.get(position).getRestaurant());
                bundle.putFloat("price", resultList.get(position).getPrice());
                startActivity(new Intent(SearchResultActivity.this, ProductDetailsActivity.class).putExtras(bundle));
            }
        });
        resultsView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
