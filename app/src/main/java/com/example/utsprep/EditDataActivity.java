package com.example.utsprep;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

public class EditDataActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText nameR;
    EditText usernameR;
    EditText birthdayR;
    EditText phoneNumR;
    EditText addressR;
    Button editdataButton;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editdata);
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
        editdataButton = findViewById(R.id.editdataButton);
        editdataButton.setOnClickListener(e -> {
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
            sp = getSharedPreferences("Users", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("name", name);
            editor.putString("username", username);
            editor.putString("birthday", birthday);
            editor.putString("phoneNum", phoneNum);
            editor.putString("address", address);
            editor.apply();

            Toast.makeText(EditDataActivity.this, "Data updated :3.",
                    Toast.LENGTH_SHORT).show();

            finish();

        });
    }
}
