package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.Adapters.CartItemAdapter;
import com.example.myapplication.Entities.CartProduct;
import com.example.myapplication.Entities.Order;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class OrderDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name, status, price;
    private RecyclerView items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        initialize();
    }

    // initializing the UI
    private void initialize(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        name = findViewById(R.id.order_name);
        status = findViewById(R.id.order_status);
        price = findViewById(R.id.order_price);
        items = findViewById(R.id.order_items);

        orderData();
    }

    // filling the order data
    private void orderData(){
        String orderName = getIntent().getStringExtra("orderName");
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("order").child(FirebaseAuth.getInstance().getUid());
        myRef.child(orderName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                    Order order = snapshot.getValue(Order.class);
                    dataInView(order);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // populating view with data
    private void dataInView(Order order){
        name.setText(order.getName());
        status.setText("Status: "+order.getStatus());
        price.setText("Total: "+order.getTotal());

        Objects.requireNonNull(getSupportActionBar()).setTitle(order.getName());

        CartItemAdapter adapter = new CartItemAdapter(this);
        adapter.setCartProductList(order.getProducts());
        items.setAdapter(adapter);
        items.setLayoutManager(new LinearLayoutManager(this));


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}