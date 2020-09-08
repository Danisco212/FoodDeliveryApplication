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
import com.example.myapplication.Entities.Order;
import com.example.myapplication.R;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private List<Order> orderList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public OrderAdapter(Context context) {
        this.context = context;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_oder, parent, false), onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        holder.name.setText(orderList.get(position).getName());
        holder.status.setText(orderList.get(position).getStatus());
        holder.price.setText("$"+orderList.get(position).getTotal());

        if (orderList.get(position).getImages().size()>1){
            holder.image2.setVisibility(View.VISIBLE);
            Glide.with(context).load(orderList.get(position).getImages().get(1)).into(holder.image2);
        }else{
            holder.image2.setVisibility(View.GONE);
        }
        Glide.with(context).load(orderList.get(position).getImages().get(0)).into(holder.image1);

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder{
        private TextView name, status, price;
        private ImageView image1, image2;
        public OrderViewHolder(@NonNull View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);

            name = itemView.findViewById(R.id.order_name);
            price = itemView.findViewById(R.id.order_price);
            status = itemView.findViewById(R.id.order_status);
            image1 = itemView.findViewById(R.id.image1);
            image2 = itemView.findViewById(R.id.image2);
        }
    }


}
