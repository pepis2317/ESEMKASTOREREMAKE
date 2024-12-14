package com.example.utsprep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements OnMapReadyCallback {
    FirebaseAuth auth;
    FirebaseUser user;
    TextView email;
    TextView name;
    TextView username;
    TextView birthday;
    TextView phoneNum;
    TextView address;
    Button logout;
    ImageView profilePic;
    Button uploadphoto;
    Button editdata;
    SharedPreferences sp;
    GoogleMap map;
    MapView mapView;
    FusedLocationProviderClient fusedLocationClient;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sp = getContext().getSharedPreferences("Users", Context.MODE_PRIVATE);
        email.setText(user.getEmail());
        name.setText(sp.getString("name", ""));
        username.setText(sp.getString("username", ""));
        phoneNum.setText(sp.getString("phoneNum", ""));
        birthday.setText(sp.getString("birthday", ""));
        address.setText(sp.getString("address", ""));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        uploadphoto = view.findViewById(R.id.uploadphoto);
        editdata = view.findViewById(R.id.editdata);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        birthday = view.findViewById(R.id.birthday);
        phoneNum = view.findViewById(R.id.phoneNum);
        address = view.findViewById(R.id.address);
        mapView = view.findViewById(R.id.mapView);

        SharedPreferences sp = getContext().getSharedPreferences("Users", Context.MODE_PRIVATE);
        String namee = sp.getString("name", "Edit your name");
        String usernamee = sp.getString("username","Edit your username");
        String birthdayy = sp.getString("birthday", "Edit your birthday");
        String phoneNumm = sp.getString("phoneNum", "Edit your phone number");
        String addresss = sp.getString("address", "Edit your address");


        logout = view.findViewById(R.id.logout);
        if(user == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        }else{
            email.setText(user.getEmail());
            name.setText(namee);
            username.setText(usernamee);
            phoneNum.setText(phoneNumm);
            birthday.setText(birthdayy);
            address.setText(addresss);

            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this::onMapReady);

        }
        uploadphotoBtn();
        editdataBtn();
        logoutBtn();
    }
    private void uploadphotoBtn(){
        uploadphoto.setOnClickListener(e->{
            Toast.makeText(getContext(),"Upload Photo",Toast.LENGTH_SHORT).show();
        });
    }

    private void editdataBtn(){
        editdata.setOnClickListener(e->{
            Intent intent = new Intent(getContext(), EditDataActivity.class);
            startActivity(intent);
        });
    }

    private void logoutBtn(){
        logout.setOnClickListener(e->{
            FirebaseAuth.getInstance().signOut();
            sp = getContext().getSharedPreferences("Users", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        getLocation();
    }

    private void getLocation() {
        // Check permission
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        SharedPreferences sp = getContext().getSharedPreferences("Users", Context.MODE_PRIVATE);
        Double latitude = Double.valueOf(sp.getString("Lat", "-7.8028333"));
        Double longitude = Double.valueOf(sp.getString("Lng", "110.374138"));

        LatLng savedLocation = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions().position(savedLocation).title("Lokasi yang tersimpan"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(savedLocation, 15));
    }

}