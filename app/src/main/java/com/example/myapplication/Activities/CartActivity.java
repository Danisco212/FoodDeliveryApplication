package com.example.myapplication.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapters.CartItemAdapter;
import com.example.myapplication.Entities.CartProduct;
import com.example.myapplication.Entities.Order;
import com.example.myapplication.Entities.User;
import com.example.myapplication.Helpers.CheckUserInput;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CartActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView errorMsg;
    private ProgressBar loading;
    private RecyclerView cartItems;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private List<CartProduct> cartProductList;

    private Float subTotal;
    private Float totalPrice;
    private Float deliveryFee = 0.99f;
    private TextView sumTotal, total, shippingcost;
    private Button checkOut;
    private EditText houseNum, streetNum, borey, province;
    private TextView addressTxt;
    private Button addressBtn;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();
    }

    private void init(){
        addressTxt = findViewById(R.id.user_address);
        addressBtn = findViewById(R.id.add_Address);

        checkOut = findViewById(R.id.checkout_btn);
        checkOut.setEnabled(false);
        sumTotal = findViewById(R.id.cart_subtotal);
        shippingcost = findViewById(R.id.cart_shippingcost);
        total = findViewById(R.id.cart_total);

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

        fillUserAddress();
        fillCart();
        getCartItems();
    }

    // get the users address
    private void fillUserAddress(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // get the result
                if (snapshot.getValue()!=null){
                    user = snapshot.getValue(User.class);
                    if (user!=null){
                        addressTxt.setText(user.getAddress());
                        addressBtn.setText("Edit Address");
                    }
                }else{ // no user entry of this user
                    Log.e("User", "There is nothing here mang");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addDeliveryAddress(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_user_address, null))
                .setPositiveButton("Save Address", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // call the firebase function to save the users address into the database
                        saveUserAddress();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        houseNum = dialog.findViewById(R.id.house_num);
        streetNum = dialog.findViewById(R.id.street_num);
        borey = dialog.findViewById(R.id.borey);
        province = dialog.findViewById(R.id.province);

        if (user!=null){
            String userAdress = user.getAddress();
            String[] addressSplit = userAdress.split(",");
            houseNum.setText(addressSplit[0]);
            streetNum.setText(addressSplit[1]);
            borey.setText(addressSplit[2]);
            province.setText(addressSplit[3]);
        }
    }

    // saving the data into the database
    private void saveUserAddress(){
        CheckUserInput checkUserInput = new CheckUserInput();
        if (checkUserInput.correctInputs(houseNum, streetNum, borey) && checkUserInput.correctInput(province)){ // correct inputs
            String userAddress = houseNum.getText().toString().trim()+","+streetNum.getText().toString().trim()+","+borey.getText().toString().trim()+","+province.getText().toString().trim();

            User user = new User();
            user.setUserId(mAuth.getUid());
            user.setAddress(userAddress);
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getUid());
            myRef.setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // show that the user address has been added
                            Toast.makeText(CartActivity.this, "Address saved!", Toast.LENGTH_LONG).show();
                            fillUserAddress();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CartActivity.this, "Something went wrong :(", Toast.LENGTH_LONG).show();
                        }
                    });


        }else{ // not correct inputs
            Toast.makeText(CartActivity.this, "Fill all the fields", Toast.LENGTH_LONG).show();
        }
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
                    calculateTotal();
                }
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /// filling the cart with cart items
    private void fillCart(){
        final CartItemAdapter adapter = new CartItemAdapter(this);
        adapter.setCartProductList(cartProductList);

        cartItems.setAdapter(adapter);
        cartItems.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper touchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                mDatabase.child(cartProductList.get(position).getId()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                calculateTotal();
                            }
                        });
            }
        });

        touchHelper.attachToRecyclerView(cartItems);
    }

    // calculating the total amount of all the items
    private void calculateTotal(){
        subTotal = 0.00f;
        boolean isOne = true;
        if (cartProductList.size()==1){
            subTotal+=cartProductList.get(0).getPrice();
        }else{
            for (int i = 0; i < cartProductList.size()-1; i++){
                if (!cartProductList.get(i).getRestaurant().equals(cartProductList.get(i+1).getRestaurant())){ // not same restaurant
                    isOne = false;
                }
                if (i==0){
                    subTotal+=cartProductList.get(i).getPrice();
                }
                subTotal+=cartProductList.get(i+1).getPrice();
            }
        }
        if (!isOne){
            deliveryFee*=2;
        }

        totalPrice = subTotal+deliveryFee;

        // display all totals
        total.setText("$"+totalPrice);
        sumTotal.setText("$"+subTotal);
        shippingcost.setText("$"+deliveryFee);
        if (subTotal>0){
            checkOut.setEnabled(true);
        }

    }

    // checkout functionality
    public void checkout(View view){
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("order").child(mAuth.getUid());
        final String ordername = "Order" + new Random().nextInt(999) + System.currentTimeMillis();
        Order order = new Order();
        order.setName(ordername);
        order.setTotal(totalPrice);
        order.setStatus("Being Prepared");
        // getting the images
        List<String> images = new ArrayList<>();
        if(cartProductList.size()==1){
            images.add(cartProductList.get(0).getImage());
        }else{
            for (int i =0; i<2; i++){
                images.add(cartProductList.get(i).getImage());
            }
        }
        order.setImages(images);
        order.setProducts(cartProductList);
        myRef.child(ordername).setValue(order)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        emptyCart();
                    }
                });

    }

    // empty cart
    private void emptyCart(){
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("cart").child(mAuth.getUid());
        myRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        CartActivity.this.onBackPressed();
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}