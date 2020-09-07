package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Entities.CartProduct;
import com.example.myapplication.Entities.Product;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class ProductDetailsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private Toolbar toolbar;
    private Product product;

    private TextView name, description, price;
    private ImageView productImage;
    private Button add, subtract;
    private EditText amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        init();
    }

    private void getProductFromIntent(){
        product = new Product();
        product.setName(getIntent().getExtras().getString("name"));
        product.setImage(getIntent().getExtras().getString("image"));
        product.setDescription(getIntent().getExtras().getString("desc"));
        product.setRestaurant(getIntent().getExtras().getString("restaurant"));
        product.setPrice(getIntent().getExtras().getFloat("price"));
        if (product!=null){
            toolbar.setTitle(product.getName());
        }
    }

    private void init(){

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("cart");

        toolbar = findViewById(R.id.toolbar);
        getProductFromIntent();
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        name = findViewById(R.id.prod_name);
        price = findViewById(R.id.prod_price);
        description = findViewById(R.id.prod_description);
        amount = findViewById(R.id.prod_amount);
        add = findViewById(R.id.add_btn);
        subtract = findViewById(R.id.subtract_btn);
        productImage = findViewById(R.id.prod_image);
        fillUpView();
        amountLogic();
    }

    // filling up the view with the information of the product
    private void fillUpView(){
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText("$"+product.getPrice());
        Glide.with(this).load(product.getImage()).into(productImage);

    }

    // cart amount logic
    private int amountNum =1;
    private void amountLogic(){

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountNum = Integer.parseInt(amount.getText().toString());
                amountNum+=1;
                amount.setText(String.valueOf(amountNum));
            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amountNum>1){
                    amountNum = Integer.parseInt(amount.getText().toString());
                    amountNum-=1;
                    amount.setText(String.valueOf(amountNum));
                }
            }
        });
    }

    // adding to cart
    public void addToCart(View view){
        if (mAuth.getCurrentUser()==null){
            // login dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("You need to be logged in")
                    .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // start the login activity here
                            startActivity(new Intent(ProductDetailsActivity.this, LoginActivity.class));
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            amountNum = Integer.parseInt(amount.getText().toString());
            if (amountNum ==0){
                amountNum =1;
            }
            for (int i =0; i<amountNum; i++){
                String cartItemId = System.currentTimeMillis()+ "-item-" + new Random().nextInt(999);
                final int j =i;
                CartProduct cartProduct = new CartProduct(cartItemId,product.getName(), product.getPrice(), product.getRestaurant(), product.getImage());
                DatabaseReference myRef = mDatabase.child(mAuth.getUid()).child(cartItemId);
                myRef.setValue(cartProduct)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                if (j==amountNum-1){
                                    ProductDetailsActivity.this.onBackPressed();
                                    Toast.makeText(ProductDetailsActivity.this, "Added to cart", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProductDetailsActivity.this, "Failed to add to cart because "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}