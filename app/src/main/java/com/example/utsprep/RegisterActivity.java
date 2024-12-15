package com.example.utsprep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button registerButton;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private TextView loginText;
    private FirebaseFirestore db ;
    private EditText nameR;
    private EditText usernameR;
    private EditText birthdayR;
    private EditText phoneNumR;
    private TextView addressR;
    private Button setAddress;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(RegisterActivity.this, BaseActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db =  FirebaseFirestore.getInstance();
        loginText = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        registerButton = findViewById(R.id.registerButton);
        nameR = findViewById(R.id.nameR);
        usernameR = findViewById(R.id.usernameR);
        birthdayR = findViewById(R.id.birthdayR);
        phoneNumR = findViewById(R.id.phoneNumR);
        addressR = findViewById(R.id.addressR);
        setAddress = findViewById(R.id.setAddress);
        setAddress.setOnClickListener(e->{
            Intent mapIntent = new Intent(RegisterActivity.this, BackActivity.class);
            mapIntent.putExtra("loadFragment", "maps");
            mapIntent.putExtra("target", "registerActivity");
            mapLauncher.launch(mapIntent);

        });
        registerButton.setOnClickListener(e->{
            String email = String.valueOf(this.email.getText());
            String password = String.valueOf(this.password.getText());
            String name = String.valueOf(this.nameR.getText());
            String username = String.valueOf(this.usernameR.getText());
            String birthday = String.valueOf(this.birthdayR.getText());
            String phoneNum = String.valueOf(this.phoneNumR.getText());
            String address = String.valueOf(this.addressR.getText());
            if(!allFieldsFilled(email, password, name, username, birthday, phoneNum, address)){
                return;
            }
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Account created :3.", Toast.LENGTH_SHORT).show();
                                FirebaseUser currentUser = mAuth.getCurrentUser();
                                if(currentUser != null){
                                    Map<String, Object> docData = new HashMap<>();
                                    docData.put("userID", currentUser.getUid());
                                    docData.put("name", name);
                                    docData.put("userName", username);
                                    docData.put("phoneNumber", phoneNum);
                                    docData.put("birthday", birthday);
                                    docData.put("addressDetail", address);
                                    db.collection("usersCollection").add(docData);
                                    Intent finishIntent = new Intent(RegisterActivity.this, BaseActivity.class);
                                    startActivity(finishIntent);
                                    finish();
                                }

                            } else {

                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        });
        loginText.setOnClickListener(e->{
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
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
    protected boolean allFieldsFilled(String email, String password, String name, String username, String birthday, String phoneNum, String address){
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Email empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Password empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, "Username empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(birthday)) {
            Toast.makeText(this, "Birthday empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phoneNum)) {
            Toast.makeText(this, "Phone Number empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (address.equals("Address not set")) {
            Toast.makeText(this, "Address not set", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}