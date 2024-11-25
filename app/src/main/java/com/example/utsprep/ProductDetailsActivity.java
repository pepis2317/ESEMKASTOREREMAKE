package com.example.utsprep;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.utsprep.models.DBHelper;
import com.example.utsprep.models.Product;

public class ProductDetailsActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private SQLiteDatabase db;
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
    int count = 1;
    private void initializeViews(){
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
        product = getIntent().getParcelableExtra("product");
        initializeViews();
        backButton();
        increment();
        decrement();

    }
}