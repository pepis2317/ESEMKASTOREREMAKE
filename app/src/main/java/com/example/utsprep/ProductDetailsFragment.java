package com.example.utsprep;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.utsprep.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProductDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetailsFragment extends Fragment {
    private LinearLayout addLayout;
    private ProgressBar pBar;
    private FirebaseFirestore db ;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private ImageView productImage;
    private TextView productTitle;
    private TextView productPrice;
    private TextView productDescription;
    private TextView productRating;
    private TextView productStock;
    private Button addToCart;
    private Product product;
    private ImageView increment;
    private ImageView decrement;
    private TextView quantity;
    private Button update;
    private int count = 1;

    public ProductDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetailsFragment.
     */

    public static ProductDetailsFragment newInstance(String param1, String param2) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_details, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db =  FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        product = getArguments().getParcelable("product");
        update = view.findViewById(R.id.update);
        productTitle = view.findViewById(R.id.productTitle);
        productTitle.setText(product.getTitle());
        productPrice = view.findViewById(R.id.productPrice);
        productPrice.setText(String.valueOf(product.getPrice()));
        productDescription = view.findViewById(R.id.productDescription);
        productDescription.setText(product.getDescription());
        productRating = view.findViewById(R.id.productRating);
        productRating.setText(String.valueOf(product.getRating().getRate()));
        productStock = view.findViewById(R.id.productStock);
        productStock.setText(String.valueOf(product.getRating().getCount()));
        productImage = view.findViewById(R.id.productImageView);
        Glide.with(this).load(product.getImage()).into(productImage);
        addToCart = view.findViewById(R.id.addToCart);
        increment = view.findViewById(R.id.increment);
        decrement = view.findViewById(R.id.decrement);
        quantity = view.findViewById(R.id.quantityTextView);
        quantity.setText(String.valueOf(count));
        pBar = view.findViewById(R.id.pBar);
        addLayout = view.findViewById(R.id.addLayout);
        String email = user.getEmail();
        String[] docId = {""};
        db.collection("cartCollection")
                .whereEqualTo("email", email) // Check if email matches
                .whereEqualTo("id", product.getId()) // Check if id matches
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        pBar.setVisibility(View.GONE);
                        addLayout.setVisibility(View.VISIBLE);
                        if (!task.getResult().isEmpty()) {
                            addToCart.setVisibility(View.GONE);
                            update.setVisibility(View.VISIBLE);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                docId[0] = document.getId();
                                count = document.getDouble("quantity").intValue();
                                quantity.setText(String.valueOf(count));
                            }

                        } else {
                            addToCart.setVisibility(View.VISIBLE);
                            update.setVisibility(View.GONE);
                        }

                    } else {
                        Log.e("FirestoreQuery", "Error getting documents: ", task.getException());
                    }
                });
        increment();
        decrement();

        update.setOnClickListener(e->{
            db.collection("cartCollection").document(docId[0]).update("quantity", count);
            Intent intent = new Intent(getContext(), BaseActivity.class);
            intent.putExtra("loadFragment", "cart");
            startActivity(intent);
            requireActivity().finish();
        });
        addToCart.setOnClickListener(e->{
            Map<String, Object> docData = new HashMap<>();
            docData.put("email", email);
            docData.put("id", product.getId());
            docData.put("title", product.getTitle());
            docData.put("price", product.getPrice());
            docData.put("description", product.getDescription());
            docData.put("category", product.getCategory());
            docData.put("image", product.getImage());
            docData.put("rate", product.getRating().getRate());
            docData.put("count", product.getRating().getCount());
            docData.put("quantity", count);
            db.collection("cartCollection").add(docData);
            Intent intent = new Intent(getContext(), BaseActivity.class);
            intent.putExtra("loadFragment", "cart");
            startActivity(intent);
            requireActivity().finish();
        });
    }
    private void increment(){
        increment.setOnClickListener(e->{
            count++;
            quantity.setText(String.valueOf(count));
        });

    }
    private void decrement(){
        decrement.setOnClickListener(e-> {
            if (count > 1) {
                count--;
                quantity.setText(String.valueOf(count));
            }
        });
    }
}