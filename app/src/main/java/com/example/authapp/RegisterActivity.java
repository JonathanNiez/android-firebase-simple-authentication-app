package com.example.authapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.authapp.utility.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPassEditText;
    private Button registerBtn;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPassEditText = findViewById(R.id.confirmPassEditText);
        registerBtn = findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        registerBtn.setOnClickListener(v -> {
            String emailStr = emailEditText.getText().toString().trim();
            String passwordStr = passwordEditText.getText().toString();
            String confirmPassStr = confirmPassEditText.getText().toString();

            if (emailStr.isEmpty() || passwordStr.isEmpty() || confirmPassStr.isEmpty()) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else if (!confirmPassStr.equals(passwordStr)) {
                Toast.makeText(this, "Password did not match", Toast.LENGTH_SHORT).show();
            } else if (passwordStr.length() < 6) {
                Toast.makeText(this, "Password at least 6 characters", Toast.LENGTH_SHORT).show();
            } else registerUser(emailStr, passwordStr);

        });
    }

    private void registerUser(String emailStr, String passwordStr) {
        progressBar.setVisibility(View.VISIBLE);
        registerBtn.setVisibility(View.GONE);
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        HashMap<String, Object> user = new HashMap<>();
                        user.put("email", emailStr);
                        user.put("userID", task.getResult().getUser().getUid());
//                        user.put("userID", FirebaseHelper.getCurrentUser().getUid());

                        FirebaseHelper.getFireStoreInstance()
                                .collection("users")
                                .document(task.getResult().getUser().getUid())
                                .set(user)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(this, "Registration failed: " + task1.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                    progressBar.setVisibility(View.GONE);
                                    registerBtn.setVisibility(View.VISIBLE);
                                });
                    } else {
                        Toast.makeText(this, "Registration failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                    registerBtn.setVisibility(View.VISIBLE);
                });
    }
}