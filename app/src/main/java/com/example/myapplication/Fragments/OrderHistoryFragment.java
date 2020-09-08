package com.example.myapplication.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.Activities.OrderDetailsActivity;
import com.example.myapplication.Adapters.OrderAdapter;
import com.example.myapplication.Entities.Order;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class OrderHistoryFragment extends Fragment {

    private RecyclerView ordersView;
    private ProgressBar loading;
    private TextView error;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private List<Order> orderList;

    public OrderHistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        initalize(view);
        return view;
    }

    private void initalize(View view) {
        error = view.findViewById(R.id.no_orders);
        orderList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("order").child(mAuth.getUid());

        ordersView = view.findViewById(R.id.user_orders);
        loading = view.findViewById(R.id.loading);

        getUserOrder();
    }

    // getting the order of the user
    private void getUserOrder(){
        loading.setVisibility(View.VISIBLE);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                error.setVisibility(View.GONE);
                orderList.clear();
                if (snapshot.getValue()==null){
                    error.setVisibility(View.VISIBLE);
                }else{
                    for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                        Log.e("order", dataSnapshot.getValue().toString());
                        Order order = dataSnapshot.getValue(Order.class);
                        orderList.add(order);
                    }
                }

                loading.setVisibility(View.GONE);
                fillOrdersView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loading.setVisibility(View.GONE);
            }
        });
    }

    // filling the orders list with some dummy data
    private void fillOrdersView(){
        OrderAdapter adapter = new OrderAdapter(this.getContext());
        adapter.setOrderList(orderList);
        adapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(OrderHistoryFragment.this.getActivity(), OrderDetailsActivity.class)
                        .putExtra("orderName", orderList.get(position).getName()));
            }
        });

        ordersView.setAdapter(adapter);
        ordersView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

}