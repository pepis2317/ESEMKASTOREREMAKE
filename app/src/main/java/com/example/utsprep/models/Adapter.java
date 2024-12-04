package com.example.utsprep.models;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.utsprep.ProductDetailsActivity;
import com.example.utsprep.R;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    List<Product> products;
    List<Product> filteredProducts;

    public Adapter(Context context, List<Product> products) {
        this.context = context;
        this.products = products;
        this.filteredProducts = new ArrayList<>(products);
    }

    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        Product product = filteredProducts.get(position);
        holder.name.setText(product.getTitle());
        holder.price.setText(String.valueOf(product.getPrice()));
        Glide.with(context).load(product.getImage()).into(holder.image);
        holder.itemView.setOnClickListener(e->{
            Intent intent = new Intent(context, ProductDetailsActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return filteredProducts.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.productImageView);
            name = itemView.findViewById(R.id.nameTextView);
            price = itemView.findViewById(R.id.priceTextView);
        }
    }
    public void filter(String query){
        filteredProducts.clear();
        if(query.isEmpty()){
            filteredProducts.addAll(products);

            Log.d("TEST", "This shit empty");
        }else{
            for (Product product : products) {
                if (product.getTitle() != null && product.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    Log.d("TEST", product.getTitle());
                    filteredProducts.add(product); // Add matching items
                }
            }
        }
        notifyDataSetChanged();
    }
}
