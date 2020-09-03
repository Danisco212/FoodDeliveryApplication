package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Activities.CartActivity;
import com.example.myapplication.Adapters.HighlightedProductsAdapter;
import com.example.myapplication.Adapters.HighlightedRestaurantsAdapter;
import com.example.myapplication.Entities.Product;
import com.example.myapplication.Entities.Restaurant;
import com.example.myapplication.R;
import com.example.myapplication.Spacing.RecyclerViewSpacing;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView restaurants, products;

    private FirebaseAuth mAuth;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initialize(view);
        return view;
    }

    private void initialize(View view){
        mAuth = FirebaseAuth.getInstance();

        restaurants = view.findViewById(R.id.restaurants);
        products = view.findViewById(R.id.highlighted_products);

        fillDummyData();
    }

    // filling up the home page with some dummy data
    private void fillDummyData(){
        List<Restaurant> restaurantList = new ArrayList<>();
        restaurantList.add(new Restaurant());
        restaurantList.add(new Restaurant());
        restaurantList.add(new Restaurant());
        restaurantList.add(new Restaurant());
        restaurantList.add(new Restaurant());

        HighlightedRestaurantsAdapter adapter = new HighlightedRestaurantsAdapter(this.getContext(), restaurantList);
        restaurants.setAdapter(adapter);
        restaurants.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        restaurants.addItemDecoration(new RecyclerViewSpacing(10));

        List<Product> productList = new ArrayList<>();
        productList.add(new Product());
        productList.add(new Product());
        productList.add(new Product());
        productList.add(new Product());
        productList.add(new Product());
        productList.add(new Product());
        productList.add(new Product());

        HighlightedProductsAdapter adapter1 = new HighlightedProductsAdapter(this.getContext(), productList);
        products.setAdapter(adapter1);
        products.setLayoutManager(new StaggeredGridLayoutManager(2,1));
    }

    // inflating the cart menu icon in the
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (mAuth.getCurrentUser()!=null){
            inflater.inflate(R.menu.menu_user_cart, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    // opening the cart activity when the cart icon is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.cart){
            startActivity(new Intent(this.getActivity(), CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}