package com.example.myapplication.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activities.ProductDetailsActivity;
import com.example.myapplication.Entities.Product;
import com.example.myapplication.Entities.Restaurant;
import com.example.myapplication.Fragments.HomeFragment;
import com.example.myapplication.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private Activity context;
    private List<String> categories;
    private Restaurant restaurant;

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public CategoryAdapter(Activity context) {
        this.context = context;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.item_restaurant_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {
        holder.name.setText(categories.get(position));
        Log.e("Test", restaurant.getCategories().get(categories.get(position)).toString());
        holder.fillCategoriesProducts(context, restaurant.getCategories().get(categories.get(position)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.wrapper.getVisibility() == View.VISIBLE){
                    holder.wrapper.setVisibility(View.GONE);
                    notifyItemChanged(position);
                }else{
                    holder.wrapper.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private RecyclerView wrapper;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            wrapper = itemView.findViewById(R.id.products);
            name = itemView.findViewById(R.id.category_name);
        }

        // filling up the categories stuffs
        private void fillCategoriesProducts(final Activity context, final List<Product> products){
            HighlightedProductsAdapter adapter = new HighlightedProductsAdapter(context, products);
            wrapper.setAdapter(adapter);
            adapter.setOnItemClickListener(new HighlightedProductsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putString("name", products.get(position).getName());
                    bundle.putString("desc", products.get(position).getDescription());
                    bundle.putString("image", products.get(position).getImage());
                    bundle.putString("restaurant", products.get(position).getRestaurant());
                    bundle.putFloat("price", products.get(position).getPrice());
                    context.startActivity(new Intent(context, ProductDetailsActivity.class).putExtras(bundle));
                }
            });
            wrapper.setLayoutManager(new GridLayoutManager(context, 2));
        }

    }
}
