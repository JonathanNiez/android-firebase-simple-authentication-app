package com.example.authapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
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

        checkCurrentUser();

        //sign out
        signOutBtn.setOnClickListener(v -> {
            FirebaseHelper.signOutUser();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void checkCurrentUser(){
        if (FirebaseHelper.getCurrentUser() != null) {
            emailTextView.setText(FirebaseHelper.getCurrentUser().getEmail());
            Log.d("MainActivity", "onCreate: " + FirebaseHelper.getCurrentUser().getUid());
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}