package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Entities.Restaurant;
import com.example.myapplication.R;

import java.util.List;

public class HighlightedRestaurantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Restaurant> restaurants;

    public HighlightedRestaurantsAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HighlightedRestaurantsViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_restaurant, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class HighlightedRestaurantsViewHolder extends RecyclerView.ViewHolder{
        public HighlightedRestaurantsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
