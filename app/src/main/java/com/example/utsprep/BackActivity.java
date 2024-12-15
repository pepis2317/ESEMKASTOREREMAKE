package com.example.utsprep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.utsprep.models.Product;

public class BackActivity extends AppCompatActivity {
    ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_back);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String fragmentToLoad = intent.getStringExtra("loadFragment");
        Product product = intent.getParcelableExtra("product");
        if(fragmentToLoad != null){
            if ( fragmentToLoad.equals("productDetails")) {
                loadProductDetailsFragment(product);
            }else if(fragmentToLoad.equals("editData")){
                loadEditDataFragment();
            }else if(fragmentToLoad.equals("maps")){
                loadMapsFragment();
            }
        }

        backArrow = findViewById(R.id.backArrow);
        backButton();
    }
    private void loadMapsFragment(){
        MapsFragment fragment = new MapsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void loadEditDataFragment(){
        EditDataFragment fragment = new EditDataFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void loadProductDetailsFragment(Product product) {
        ProductDetailsFragment fragment = new ProductDetailsFragment();

        // Pass the Product data to the Fragment using a Bundle
        Bundle bundle = new Bundle();
        bundle.putParcelable("product",product);
        fragment.setArguments(bundle);

        // Replace the FrameLayout with the fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment)
                .commit();
    }
    private void backButton(){
        backArrow.setOnClickListener(e->{
            finish();
        });
    }
}