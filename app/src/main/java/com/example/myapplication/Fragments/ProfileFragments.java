package com.example.myapplication.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myapplication.Activities.MainActivity;
import com.example.myapplication.Adapters.OrderAdapter;
import com.example.myapplication.Entities.Order;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ProfileFragments extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private RecyclerView ordersView;
    private TextView error, email, totalSpent;

    public ProfileFragments() {
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
        View view = inflater.inflate(R.layout.fragment_profile_fragments, container, false);

        init(view);
        return view;
    }

    private void init(View view){
        ordersView = view.findViewById(R.id.recentOrders);
        error = view.findViewById(R.id.profile_no_orders);
        email = view.findViewById(R.id.profile_email);
        totalSpent = view.findViewById(R.id.profile_total);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("order").child(mAuth.getUid());


        email.setText(mAuth.getCurrentUser().getEmail());

        getUserRecentOrders();
    }


    // showing the user cart
    private void getUserRecentOrders(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                error.setVisibility(View.GONE);
                List<Order> orderList = new ArrayList<>();
                if (snapshot.getValue()==null){
                    error.setVisibility(View.VISIBLE);
                }else{
                    float total = 0;
                    int size =5;
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        if (size==0){
                            break;
                        }
                        Log.e("order", dataSnapshot.getValue().toString());
                        Order order = dataSnapshot.getValue(Order.class);
                        total+=order.getTotal();
                        orderList.add(order);
                        size-=1;
                    }

                    totalSpent.setText("Total spent: $"+ total);
                }
                fillOrdersView(orderList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void fillOrdersView(List<Order> orderList){
        OrderAdapter adapter = new OrderAdapter(this.getContext());
        adapter.setOrderList(orderList);

        ordersView.setAdapter(adapter);
        ordersView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
                builder.setMessage("Are you sure?")
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mAuth.signOut();
                                startActivity(new Intent(ProfileFragments.this.getActivity(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                builder.create().show();
                break;
            case R.id.settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}