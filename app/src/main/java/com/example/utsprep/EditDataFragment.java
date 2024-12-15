package com.example.utsprep;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditDataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditDataFragment extends Fragment {
    private EditText nameR;
    private EditText usernameR;
    private EditText birthdayR;
    private EditText phoneNumR;
    private TextView addressR;
    private Button setAddress;
    private Button editdataButton;
    private FirebaseFirestore db ;
    private FirebaseAuth auth;
    private FirebaseUser user;


    public EditDataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditDataFragment.
     */

    public static EditDataFragment newInstance(String param1, String param2) {
        EditDataFragment fragment = new EditDataFragment();
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
        return inflater.inflate(R.layout.fragment_edit_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        nameR = view.findViewById(R.id.nameR);
        usernameR = view.findViewById(R.id.usernameR);
        birthdayR = view.findViewById(R.id.birthdayR);
        phoneNumR = view.findViewById(R.id.phoneNumR);
        addressR = view.findViewById(R.id.addressR);
        setAddress = view.findViewById(R.id.setAddress);
        editdataButton = view.findViewById(R.id.editdataButton);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db.collection("usersCollection").whereEqualTo("userID", user.getUid()).get().addOnCompleteListener(task->{
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                String fetchedName = document.getString("name");
                String fetchedUsername = document.getString("userName");
                String fetchedPhoneNum = document.getString("phoneNumber");
                String fetchedBirthday = document.getString("birthday");
                String fetchedAddress = document.getString("addressDetail");
                nameR.setText(fetchedName);
                usernameR.setText(fetchedUsername);
                phoneNumR.setText(fetchedPhoneNum);
                birthdayR.setText(fetchedBirthday);
                addressR.setText(fetchedAddress);
            }
        });
        setAddress.setOnClickListener(e->{
            Intent mapIntent = new Intent(getContext(), BackActivity.class);
            mapIntent.putExtra("loadFragment", "maps");
            mapIntent.putExtra("target", "editData");
            mapLauncher.launch(mapIntent);
        });
        editdataButton.setOnClickListener(e -> {
            String name = String.valueOf(this.nameR.getText());
            String username = String.valueOf(this.usernameR.getText());
            String birthday = String.valueOf(this.birthdayR.getText());
            String phoneNum = String.valueOf(this.phoneNumR.getText());
            String address = String.valueOf(this.addressR.getText());
            if(!allFieldsFilled(name, username, birthday, phoneNum, address)){
                return;
            }
            db.collection("usersCollection").whereEqualTo("userID", user.getUid()).get().addOnCompleteListener(task->{
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                    document.getReference().update("name", name);
                    document.getReference().update("userName", username);
                    document.getReference().update("birthday", birthday);
                    document.getReference().update("phoneNumber", phoneNum);
                    document.getReference().update("addressDetail", address);
                }
            });


            Toast.makeText(getContext(), "Data updated :3.", Toast.LENGTH_SHORT).show();

            requireActivity().finish();

        });
    }
    private ActivityResultLauncher<Intent> mapLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String address = data.getStringExtra("address");
                        addressR.setText(address);
                    }
                }
            });
    protected boolean allFieldsFilled(String name, String username, String birthday, String phoneNum, String address){
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getContext(), "Name empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getContext(), "Username empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(birthday)) {
            Toast.makeText(getContext(), "Birthday empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(getContext(), "Phone Number empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (address.equals("Address not set")) {
            Toast.makeText(getContext(), "Address not set", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}