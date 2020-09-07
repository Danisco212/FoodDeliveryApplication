package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapters.CartItemAdapter;
import com.example.myapplication.Entities.CartProduct;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView errorMsg;
    private ProgressBar loading;
    private RecyclerView cartItems;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private List<CartProduct> cartProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();
    }

    private void init(){
        cartProductList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("cart").child(mAuth.getUid());

        loading = findViewById(R.id.loading);
        errorMsg = findViewById(R.id.empty_cart);
        cartItems = findViewById(R.id.cartView);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Cart");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        fillCart();
        getCartItems();
    }

    // getting the cart items from the database
    private void getCartItems(){
        loading.setVisibility(View.VISIBLE);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cartProductList.clear();
                if (snapshot.getValue() == null){
                    Toast.makeText(CartActivity.this, "No items in cart", Toast.LENGTH_LONG).show();
                }else{
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        CartProduct cartProduct = dataSnapshot.getValue(CartProduct.class);
                        cartProductList.add(cartProduct);
                    }
                    fillCart();
                }
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /// filling the cart with dummy data
    private void fillCart(){
        CartItemAdapter adapter = new CartItemAdapter(this);
        adapter.setCartProductList(cartProductList);

        cartItems.setAdapter(adapter);
        cartItems.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}