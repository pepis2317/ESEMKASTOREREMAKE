package com.example.utsprep;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment  {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView email;
    private TextView name;
    private TextView username;
    private TextView birthday;
    private TextView phoneNum;
    private TextView address;
    private Button logout;
    private FirebaseFirestore db ;
    private ImageView profilePic;
    private Button uploadphoto;
    private Button editdata;
    private LinearLayout pBar;
    private LinearLayout profile;



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

    }
    @Override
    public void onResume(){
        super.onResume();
        fetchData();
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
        profile = view.findViewById(R.id.profile);
        pBar = view.findViewById(R.id.pBar);
        uploadphoto = view.findViewById(R.id.uploadphoto);
        editdata = view.findViewById(R.id.editdata);
        db =  FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        birthday = view.findViewById(R.id.birthday);
        phoneNum = view.findViewById(R.id.phoneNum);
        address = view.findViewById(R.id.address);
        logout = view.findViewById(R.id.logout);
        if(user == null){
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        }else{
            fetchData();

        }
        uploadphotoBtn();
        editdataBtn();
        logoutBtn();
    }
    private void fetchData(){
        db.collection("usersCollection").whereEqualTo("userID", user.getUid()).get().addOnCompleteListener(task->{
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                String fetchedName = document.getString("name");
                String fetchedUsername = document.getString("userName");
                String fetchedPhoneNum = document.getString("phoneNumber");
                String fetchedBirthday = document.getString("birthday");
                String fetchedAddress = document.getString("addressDetail");

                email.setText(user.getEmail());
                name.setText(fetchedName);
                username.setText(fetchedUsername);
                phoneNum.setText(fetchedPhoneNum);
                birthday.setText(fetchedBirthday);
                address.setText(fetchedAddress);
                pBar.setVisibility(View.GONE);
                profile.setVisibility(View.VISIBLE);
            }
        });
    }
    private void uploadphotoBtn(){
        uploadphoto.setOnClickListener(e->{
            Toast.makeText(getContext(),"Upload Photo",Toast.LENGTH_SHORT).show();
        });
    }

    private void editdataBtn(){
        editdata.setOnClickListener(e->{
            Intent intent = new Intent(getContext(), BackActivity.class);
            intent.putExtra("loadFragment", "editData");
            getContext().startActivity(intent);
        });
    }

    private void logoutBtn(){
        logout.setOnClickListener(e->{
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });
    }


}