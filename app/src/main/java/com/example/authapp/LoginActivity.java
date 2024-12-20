package com.example.authapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.authapp.utility.FirebaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button loginBtn, nextBtn;
    private EditText emailEditText, passwordEditText;
    private ProgressBar progressBar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginBtn = findViewById(R.id.loginBtn);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nextBtn = findViewById(R.id.nextBtn);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        nextBtn.setOnClickListener(v -> {
            intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });

        loginBtn.setOnClickListener(v -> {
            String emailStr = emailEditText.getText().toString().trim();
            String passwordStr = passwordEditText.getText().toString();
            if (emailStr.isEmpty() || passwordStr.isEmpty()) {
                Toast.makeText(this, "Fields must not be empty", Toast.LENGTH_SHORT).show();
            } else loginUser(emailStr, passwordStr);
        });
    }

    private void loginUser(String emailStr, String passwordStr) {
        progressBar.setVisibility(View.VISIBLE);
        loginBtn.setVisibility(View.GONE);
        FirebaseHelper.getAuth().signInWithEmailAndPassword(emailStr, passwordStr)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("LoginActivity", "Login Success");
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("LoginActivity", "Failed to login: " + task.getException());
                    }
                    progressBar.setVisibility(View.GONE);
                    loginBtn.setVisibility(View.VISIBLE);
                });
    }
}