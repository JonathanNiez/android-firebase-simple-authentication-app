package com.example.authapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.authapp.utility.FirebaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private TextView emailTextView;
    private Button signOutBtn;
    private ProgressBar progressBar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailTextView = findViewById(R.id.emailTextView);
        signOutBtn = findViewById(R.id.signOutBtn);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        checkCurrentUser();

        signOutBtn.setOnClickListener(v -> signOutUser());
    }

    private void checkCurrentUser() {
        if (FirebaseHelper.getCurrentUser() != null) {
            emailTextView.setText(FirebaseHelper.getCurrentUser().getEmail());
            Log.d("MainActivity", "USER ID: " + FirebaseHelper.getCurrentUser().getUid());
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void signOutUser() {
        progressBar.setVisibility(View.VISIBLE);
        signOutBtn.setVisibility(View.GONE);
        try {
            FirebaseHelper.signOutUser();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            signOutBtn.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Failed to log out: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Failed to log out", e);
        }
    }
}