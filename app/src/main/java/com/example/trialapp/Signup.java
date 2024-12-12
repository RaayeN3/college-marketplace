package com.example.trialapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class Signup extends AppCompatActivity {
    TextInputEditText emailText, passwordText, nameText, phoneText;
    Button buttonSignup;
    FirebaseAuth mAuth;
    DatabaseReference userDbRef;
    String userId;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is already signed in and navigate to MainActivity
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToMainActivity();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Initialize FirebaseAuth and other views
        mAuth = FirebaseAuth.getInstance();
        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        nameText = findViewById(R.id.full_name);
        phoneText = findViewById(R.id.phone_num);
        buttonSignup = findViewById(R.id.signup_button);

        // Initialize DatabaseReference to Firebase Realtime Database
        userDbRef = FirebaseDatabase.getInstance().getReference("users");

        // Set up button click listener for signup
        buttonSignup.setOnClickListener(view -> registerUser());
    }

    private void registerUser() {
        String email = String.valueOf(emailText.getText()).trim();
        String password = String.valueOf(passwordText.getText()).trim();
        String fullName = String.valueOf(nameText.getText()).trim();
        String phoneNumber = String.valueOf(phoneText.getText()).trim();

        // Input validation
        if (TextUtils.isEmpty(email)) {
            showToast("Enter email");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast("Enter password");
            return;
        }
        if (TextUtils.isEmpty(fullName)) {
            showToast("Enter full name");
            return;
        }
        if (TextUtils.isEmpty(phoneNumber)) {
            showToast("Enter phone number");
            return;
        }

        // Create a new user in Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // User account successfully created
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            userId = user.getUid();
                            Users newUser = new Users(fullName, phoneNumber);

                            // Save user details in Firebase Realtime Database
                            userDbRef.child(userId).setValue(newUser)
                                    .addOnSuccessListener(aVoid -> {
                                        showToast("Account created and data saved");
                                        navigateToMainActivity();
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e("Signup", "Error saving user data", e);
                                        showToast("Failed to save user data");
                                    });
                        }
                    } else {
                        // If account creation fails, show the error message
                        Log.e("Signup", "Authentication failed", task.getException());
                        showToast("Authentication failed: " + Objects.requireNonNull(task.getException()).getMessage());
                    }
                });
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(Signup.this, message, Toast.LENGTH_SHORT).show();
    }
}
