package com.example.utsprep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity {
    ImageButton homeButton;
    ImageButton cartButton;
    ImageButton profileButton;
    TextView   textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_base);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        homeButton = findViewById(R.id.homeButton);
        cartButton = findViewById(R.id.cartButton);
        profileButton = findViewById(R.id.profileButton);
        textView = findViewById(R.id.textView);


        // Default fragment when activity starts
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new HomeFragment())
                .commit();
        String fragmentToLoad = getIntent().getStringExtra("loadFragment");
        if ("cart".equals(fragmentToLoad)) {
            replaceFragment(new CartFragment());
        }
        // Set up click listeners for buttons
        homeButton.setOnClickListener(e -> {
            replaceFragment(new HomeFragment());
        });

        cartButton.setOnClickListener(e -> {
            replaceFragment(new CartFragment());
        });

        profileButton.setOnClickListener(e -> {
            replaceFragment(new ProfileFragment());
        });

    }
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
    protected void setContent(int layoutResID){
        FrameLayout container = findViewById(R.id.container);
        getLayoutInflater().inflate(layoutResID, container, true);

    }
}