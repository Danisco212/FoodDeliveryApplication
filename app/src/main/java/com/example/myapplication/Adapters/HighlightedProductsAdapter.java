package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Entities.Product;
import com.example.myapplication.R;

import java.util.List;

public class HighlightedProductsAdapter extends RecyclerView.Adapter<HighlightedProductsAdapter.HighlightedProductViewHolder> {

    private Context context;
    private List<Product> products;

    public HighlightedProductsAdapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public HighlightedProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HighlightedProductViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_highlighted_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HighlightedProductViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class HighlightedProductViewHolder extends RecyclerView.ViewHolder{
        public HighlightedProductViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
