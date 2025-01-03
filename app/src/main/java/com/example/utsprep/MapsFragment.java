package com.example.utsprep;

import static android.app.Activity.RESULT_OK;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsFragment extends Fragment {
    private Class<?> targetActivity;

    private LatLng currentLocation;
    private Button setLocation;
    private Marker marker;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            currentLocation = new LatLng(-7.8028333,110.374138  );
//            marker = googleMap.addMarker(new MarkerOptions().position(currentLocation));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 19));
            googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    LatLng cameraTarget = googleMap.getCameraPosition().target;
                    currentLocation = cameraTarget;
//                    marker.setPosition(cameraTarget);
                }
            });
        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        Intent targetActivityIntent = requireActivity().getIntent();
        String targetActivityName = targetActivityIntent.getStringExtra("target");
        if(!targetActivityName.isEmpty()){
            if(targetActivityName.equals("registerActivity")){
                targetActivity = RegisterActivity.class;
            }else if(targetActivityName.equals("editData")){
                targetActivity = BackActivity.class;
            }
        }
        setLocation = view.findViewById(R.id.setLocation);
        setLocation.setOnClickListener(e -> {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(currentLocation.latitude, currentLocation.longitude, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    String addressString = addresses.get(0).getAddressLine(0);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("address", addressString);

                    requireActivity().setResult(Activity.RESULT_OK, returnIntent);
                    requireActivity().finish();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                Toast.makeText(getContext(), "Failed to get address", Toast.LENGTH_SHORT).show();
            }
        });

    }
}