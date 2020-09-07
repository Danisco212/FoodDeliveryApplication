package com.example.myapplication.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Entities.Product;
import com.example.myapplication.R;

import java.util.List;

public class HighlightedProductsAdapter extends RecyclerView.Adapter<HighlightedProductsAdapter.HighlightedProductViewHolder> {

    private Context context;
    private List<Product> products;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public HighlightedProductsAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public HighlightedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HighlightedProductViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_highlighted_product, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightedProductViewHolder holder, int position) {
        holder.rootView.setCardBackgroundColor(Color.parseColor(products.get(position).getColor()));
        if (products.get(position).getImage().contains("https")){
            Glide.with(context).load(products.get(position).getImage()).into(holder.image);
        }
        holder.price.setText("$"+ products.get(position).getPrice());
        holder.name.setText(products.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class HighlightedProductViewHolder extends RecyclerView.ViewHolder{
        private CardView rootView;
        private TextView price, name;
        private ImageView image;
        public HighlightedProductViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            rootView = itemView.findViewById(R.id.rootView);
            price = itemView.findViewById(R.id.food_price);
            name = itemView.findViewById(R.id.food_name);
            image = itemView.findViewById(R.id.food_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = HighlightedProductViewHolder.this.getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}
