package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.Activities.CartActivity;
import com.example.myapplication.Activities.ProductDetailsActivity;
import com.example.myapplication.Activities.RestaurantActivity;
import com.example.myapplication.Activities.SearchResultActivity;
import com.example.myapplication.Adapters.HighlightedProductsAdapter;
import com.example.myapplication.Adapters.HighlightedRestaurantsAdapter;
import com.example.myapplication.Entities.Product;
import com.example.myapplication.Entities.Restaurant;
import com.example.myapplication.R;
import com.example.myapplication.Spacing.RecyclerViewSpacing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private EditText searchBox;
    private RecyclerView restaurants, products;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private List<Restaurant> restaurantList;
    private List<Product> productList;

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
        searchBox = view.findViewById(R.id.searchBox);
        searchForProduct();
        restaurantList = new ArrayList<>();
        productList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("restaurant");

        restaurants = view.findViewById(R.id.restaurants);
        products = view.findViewById(R.id.highlighted_products);

        getRestaurants();
        fillProduct("Burger King");
    }

    // this function handles when the user presses enter to search for the product that they want
    private void searchForProduct(){
        TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    // send the term to the results activity and find the products
                    if (!searchBox.getText().toString().trim().equals("")){
                        startActivity(new Intent(HomeFragment.this.getActivity(), SearchResultActivity.class)
                                .putExtra("search", searchBox.getText().toString()));
                    }
                }
                return false;
            }
        };

        searchBox.setOnEditorActionListener(actionListener);

    }

    private void getRestaurants(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                restaurantList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    Restaurant restaurant = snapshot1.getValue(Restaurant.class);
                    restaurantList.add(restaurant);
//                    Log.e("Tag", restaurant.toString());
                }
                fillRestaurantData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // filling up the home page with some dummy data
    private void fillRestaurantData(){
        HighlightedRestaurantsAdapter adapter = new HighlightedRestaurantsAdapter(this.getContext(), restaurantList);
        restaurants.setAdapter(adapter);

        adapter.setOnItemClickListener(new HighlightedRestaurantsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(HomeFragment.this.getActivity(), RestaurantActivity.class)
                .putExtra("name", restaurantList.get(position).getName()));
            }
        });

        restaurants.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.HORIZONTAL, false));
        if(restaurants.getItemDecorationCount()<=0){
            restaurants.addItemDecoration(new RecyclerViewSpacing(10));
        }
    }

    // filling up the products list based on the restaurant
    private void fillProduct(final String restaurantname){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    Restaurant restaurant = snapshot1.getValue(Restaurant.class);
                    if (restaurant.getName().equals(restaurantname)){
                        if (restaurant.getCategories()!=null){
                            for (String key: restaurant.getCategories().keySet()){
                                for (Product product: restaurant.getCategories().get(key)){
                                    if (product!=null){
                                        productList.add(product);
//                                        Log.e("Product:", "Category"+key+" - "+ product.toString());
                                    }
                                }
                            }
                            productsView();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // setting up products recycler view
    private void productsView(){
        HighlightedProductsAdapter adapter = new HighlightedProductsAdapter(this.getContext(), productList);
        adapter.setOnItemClickListener(new HighlightedProductsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putString("name", productList.get(position).getName());
                bundle.putString("desc", productList.get(position).getDescription());
                bundle.putString("image", productList.get(position).getImage());
                bundle.putString("restaurant", productList.get(position).getRestaurant());
                bundle.putFloat("price", productList.get(position).getPrice());
                startActivity(new Intent(HomeFragment.this.getActivity(), ProductDetailsActivity.class).putExtras(bundle));
            }
        });
        products.setAdapter(adapter);
        products.setLayoutManager(new GridLayoutManager(this.getContext(),2));
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