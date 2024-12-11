package com.example.utsprep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RegisterActivity2 extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText nameR;
    EditText usernameR;
    EditText birthdayR;
    EditText phoneNumR;
    EditText addressR;
    Button registerButton;
    SharedPreferences sp;
//    GoogleMap map;
//    MapView mapView;
//    FusedLocationProviderClient fusedLocationClient;
//    TextView tapTextView;
//    TextView cameraTextView;

//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(RegisterActivity2.this, BaseActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register2);
        mAuth = FirebaseAuth.getInstance();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        nameR = findViewById(R.id.nameR);
        usernameR = findViewById(R.id.usernameR);
        birthdayR = findViewById(R.id.birthdayR);
        phoneNumR = findViewById(R.id.phoneNumR);
        addressR = findViewById(R.id.addressR);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(e -> {
            String name = String.valueOf(this.nameR.getText());
            String username = String.valueOf(this.usernameR.getText());
            String birthday = String.valueOf(this.birthdayR.getText());
            String phoneNum = String.valueOf(this.phoneNumR.getText());
            String address = String.valueOf(this.addressR.getText());
            if (TextUtils.isEmpty(name)) {
                Toast.makeText(this, "Name empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(username)) {
                Toast.makeText(this, "Username empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(birthday)) {
                Toast.makeText(this, "Birthday empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(phoneNum)) {
                Toast.makeText(this, "Phone Number empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(address)) {
                Toast.makeText(this, "Address empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Places.isInitialized()) {
                Places.initialize(getApplicationContext(), "AIzaSyDViBc35cqGGAolX-8eY6l96lbY5p0b0nc");
            }
            PlacesClient placesClient = Places.createClient(this);

            // Setup Google Map
//            mapView = findViewById(R.id.mapView);
//
//            mapView.onCreate(savedInstanceState);
//            mapView.getMapAsync(this);
//
//            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//            MapsInitializer.initialize(this);

            sp = getSharedPreferences("Users", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("name", name);
            editor.putString("username", username);
            editor.putString("birthday", birthday);
            editor.putString("phoneNum", phoneNum);
            editor.putString("address", address);
            editor.apply();

            Toast.makeText(RegisterActivity2.this, "Data saved :3.",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(RegisterActivity2.this, BaseActivity.class);
            startActivity(intent);
            finish();

        });
    }

//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        map = googleMap;
//    }
}