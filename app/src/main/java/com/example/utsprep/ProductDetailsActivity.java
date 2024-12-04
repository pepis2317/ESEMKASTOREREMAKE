package com.example.utsprep;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.utsprep.models.CartItem;
import com.example.utsprep.models.DBHelper;
import com.example.utsprep.models.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {
    LinearLayout addLayout;
    ProgressBar pBar;
    FirebaseFirestore db ;
    FirebaseAuth auth;
    FirebaseUser user;
    ImageView productImage;
    TextView productTitle;
    TextView productPrice;
    TextView productDescription;
    TextView productRating;
    TextView productStock;
    Button addToCart;
    Product product;
    ImageView backArrow;
    ImageView increment;
    ImageView decrement;
    TextView quantity;
    Button update;
    int count = 1;
    private void initializeViews(){
        update = findViewById(R.id.update);
        productTitle = findViewById(R.id.productTitle);
        productTitle.setText(product.getTitle());
        productPrice = findViewById(R.id.productPrice);
        productPrice.setText(String.valueOf(product.getPrice()));
        productDescription = findViewById(R.id.productDescription);
        productDescription.setText(product.getDescription());
        productRating = findViewById(R.id.productRating);
        productRating.setText(String.valueOf(product.getRating().getRate()));
        productStock = findViewById(R.id.productStock);
        productStock.setText(String.valueOf(product.getRating().getCount()));
        productImage = findViewById(R.id.productImageView);
        Glide.with(this).load(product.getImage()).into(productImage);
        backArrow = findViewById(R.id.backArrow);
        addToCart = findViewById(R.id.addToCart);
        increment = findViewById(R.id.increment);
        decrement = findViewById(R.id.decrement);
        quantity = findViewById(R.id.quantityTextView);
        quantity.setText(String.valueOf(count));
        pBar = findViewById(R.id.pBar);
        addLayout = findViewById(R.id.addLayout);

    }
    private void backButton(){
        backArrow.setOnClickListener(e->{
            finish();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db =  FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        product = getIntent().getParcelableExtra("product");
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
        initializeViews();
        backButton();
        increment();
        decrement();

        update.setOnClickListener(e->{
            db.collection("cartCollection").document(docId[0]).update("quantity", count);
            Intent intent = new Intent(ProductDetailsActivity.this, BaseActivity.class);
            intent.putExtra("loadFragment", "cart");
            startActivity(intent);
            finish();
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
            Intent intent = new Intent(ProductDetailsActivity.this, BaseActivity.class);
            intent.putExtra("loadFragment", "cart");
            startActivity(intent);
            finish();
        });

    }

}