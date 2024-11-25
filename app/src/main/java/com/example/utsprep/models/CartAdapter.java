package com.example.utsprep.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.utsprep.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    List<CartItem> items;
    public CartAdapter(Context context, List<CartItem> items){
        this.context = context;
        this.items = items;
    }
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.name.setText(item.getProduct().getTitle());
        holder.price.setText(String.valueOf(item.getProduct().getPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        Glide.with(context).load(item.getProduct().getImage()).into(holder.image);

        holder.increment.setOnClickListener(e->{
            item.setQuantity(item.getQuantity()+1);
            holder.quantity.setText(String.valueOf(item.getQuantity()));
        });
        holder.decrement.setOnClickListener(e->{
            if(item.getQuantity()>1){
                item.setQuantity(item.getQuantity()-1);
                holder.quantity.setText(String.valueOf(item.getQuantity()));
            }
        });
        holder.delete.setOnClickListener(e->{
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        ImageView image;
        TextView quantity;
        ImageView productImage;
        ImageView increment;
        ImageView decrement;
        Button delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.nameTextView);
            this.price = itemView.findViewById(R.id.priceTextView);
            this.image = itemView.findViewById(R.id.productImageView);
            this.quantity = itemView.findViewById(R.id.quantityTextView);
            this.increment = itemView.findViewById(R.id.increment);
            this.decrement = itemView.findViewById(R.id.decrement);
            this.productImage = itemView.findViewById(R.id.productImageView);
            this.delete = itemView.findViewById(R.id.deleteButton);
        }
    }
}
