package com.example.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Entities.CartProduct;
import com.example.myapplication.R;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<CartProduct> cartProductList;
    private Context context;

    public CartItemAdapter(Context context) {
        this.context = context;
    }

    public void setCartProductList(List<CartProduct> cartProductList) {
        this.cartProductList = cartProductList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CartItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        if (cartProductList.get(position).getImage().contains("https")){
            Glide.with(context).load(cartProductList.get(position).getImage()).into(holder.image);
        }
        holder.name.setText(cartProductList.get(position).getName());
        holder.price.setText("$"+cartProductList.get(position).getPrice());
        holder.restaurant.setText("From - "+cartProductList.get(position).getRestaurant());
    }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }

    public static class CartItemViewHolder extends RecyclerView.ViewHolder{

        private TextView name, restaurant, price;
        private ImageView image;
        public CartItemViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cart_prod_name);
            restaurant = itemView.findViewById(R.id.cart_restaurant);
            price = itemView.findViewById(R.id.cart_prod_price);
            image = itemView.findViewById(R.id.cart_image);
        }
    }
}
