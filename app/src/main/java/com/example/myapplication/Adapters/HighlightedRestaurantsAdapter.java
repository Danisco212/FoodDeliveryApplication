package com.example.myapplication.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Entities.Restaurant;
import com.example.myapplication.R;

import java.util.List;

public class HighlightedRestaurantsAdapter extends RecyclerView.Adapter<HighlightedRestaurantsAdapter.HighlightedRestaurantsViewHolder> {

    private Context context;
    private List<Restaurant> restaurants;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public HighlightedRestaurantsAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }


    @NonNull
    @Override
    public HighlightedRestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HighlightedRestaurantsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightedRestaurantsViewHolder holder, int position) {
        holder.restaurantName.setText(restaurants.get(position).getName());
        if (restaurants.get(position).getImage().contains("https")){
            Glide.with(context).load(restaurants.get(position).getImage()).into(holder.restaurantImage);
//            Log.e("image", restaurants.get(position).getImage());
        }
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public static class HighlightedRestaurantsViewHolder extends RecyclerView.ViewHolder{
        private TextView restaurantName;
        private ImageView restaurantImage;
        public HighlightedRestaurantsViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurant_image);
            restaurantName = itemView.findViewById(R.id.restaurant_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = HighlightedRestaurantsViewHolder.this.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
