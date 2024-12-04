package com.example.utsprep.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.utsprep.OnQuantityChangeListener;
import com.example.utsprep.ProductDetailsActivity;
import com.example.utsprep.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    List<CartItem> items;
    OnQuantityChangeListener listener;
    public CartAdapter(Context context, List<CartItem> items, OnQuantityChangeListener listener){
        this.context = context;
        this.items = items;
        this.listener = listener;
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
        holder.defaultQuantity = item.getQuantity();
        holder.itemView.setOnClickListener(e->{
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("product", item.getProduct());
            context.startActivity(intent);
        });
        holder.name.setText(item.getProduct().getTitle());
        holder.price.setText(String.valueOf(item.getProduct().getPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        Glide.with(context).load(item.getProduct().getImage()).into(holder.image);

        holder.increment.setOnClickListener(e->{
            item.setQuantity(item.getQuantity()+1);
            holder.quantity.setText(String.valueOf(item.getQuantity()));
            if(holder.defaultQuantity != item.getQuantity()){
                holder.update.setVisibility(View.VISIBLE);
            }else{
                holder.update.setVisibility(View.GONE);
            }
        });
        holder.decrement.setOnClickListener(e->{
            if(item.getQuantity()>1){
                item.setQuantity(item.getQuantity()-1);
                holder.quantity.setText(String.valueOf(item.getQuantity()));
            }
            if(holder.defaultQuantity != item.getQuantity()){
                holder.update.setVisibility(View.VISIBLE);
            }else{
                holder.update.setVisibility(View.GONE);
            }

        });
        holder.delete.setOnClickListener(e->{
            FirebaseFirestore db =  FirebaseFirestore.getInstance();
            db.collection("cartCollection").document(item.getId()).delete();
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
            if (listener != null) listener.onQuantityChanged();

        });
        holder.update.setOnClickListener(e->{
            FirebaseFirestore db =  FirebaseFirestore.getInstance();
            db.collection("cartCollection").document(item.getId()).update("quantity", item.getQuantity());
            holder.defaultQuantity = item.getQuantity();
            holder.update.setVisibility(View.GONE);
            if (listener != null) listener.onQuantityChanged();
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
        Button update;
        int defaultQuantity;
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
            this.update = itemView.findViewById(R.id.updateButton);
        }
    }
}
