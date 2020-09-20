package com.example.myapplication.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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


public class MonitorOrderFragment extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private TextView orderName, orderStat;
    private ImageView orderStatus;
    private RecyclerView ordersView;
    private ProgressBar loading;

    public MonitorOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monitor_order, container, false);

        initialize(view);
        return view;
    }

    private void initialize(View view){
        loading = view.findViewById(R.id.loading);
        ordersView = view.findViewById(R.id.pendingOrders);
        orderName = view.findViewById(R.id.order_name);
        orderStatus = view.findViewById(R.id.order_status);
        orderStat = view.findViewById(R.id.order_stat);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("order").child(mAuth.getUid());
        getPendingOrders();
    }

    // getting the users pending orders
    private void getPendingOrders(){
        loading.setVisibility(View.VISIBLE);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                    List<Order> orderList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        if (!dataSnapshot.getValue(Order.class).getStatus().equals("Delivered")){
                            orderList.add(dataSnapshot.getValue(Order.class));
                        }
                    }
                    loading.setVisibility(View.GONE);
                    fillRecyclerView(orderList);
                }else{
                    loading.setVisibility(View.GONE);
                    orderName.setText("You have no pending orders");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // filling recyclerview
    private void fillRecyclerView(final List<Order> orders){
        OrderAdapter adapter = new OrderAdapter(this.getContext());
        adapter.setOrderList(orders);

        // clicking the order to show its position
        adapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                orderName.setText(orders.get(position).getName());
                if(orders.get(position).getStatus().equals("Being Prepared")){
                    orderStatus.setImageResource(R.drawable.being_prepared);
                }else if (orders.get(position).getStatus().equals("Waiting for payment")){
                    orderStatus.setImageResource(R.drawable.arrived);
                }else{
                    orderStatus.setImageResource(R.drawable.in_transit);
                }
//                switch (orders.get(position).getStatus()){
//                    case "Being Prepared":
//                        orderStatus.setImageResource(R.drawable.being_prepared);
//                        break;
//                    case "In transit":
//                        orderStatus.setImageResource(R.drawable.in_transit);
//                        break;
//                    case "Waiting for payment":
//                        orderStatus.setImageResource(R.drawable.arrived);
//                        break;
//                    default:
//                        break;
//                }
                orderStat.setText("Status: "+orders.get(position).getStatus());
            }
        });

        ordersView.setAdapter(adapter);
        ordersView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }
}