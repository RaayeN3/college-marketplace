package com.example.trialapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    TextInputEditText emailText,passwordText,nameText,phoneText;
    Button button_signup;
    FirebaseAuth mAuth;
    DatabaseReference userdbref;
    String userId;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        mAuth=FirebaseAuth.getInstance();
        emailText=findViewById(R.id.email);
        passwordText=findViewById(R.id.password);
        nameText=findViewById(R.id.full_name);
        phoneText=findViewById(R.id.phone_num);
        button_signup=findViewById(R.id.signup_button);

        userdbref= FirebaseDatabase.getInstance().getReference().child("users");
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email_s,password_s,name_s,phone_s;
                email_s = String.valueOf(emailText.getText());
                password_s = String.valueOf(passwordText.getText());
                name_s=String.valueOf(nameText.getText());
                phone_s=String.valueOf(phoneText.getText());

                Users user=new Users(name_s,phone_s);

                if(TextUtils.isEmpty(email_s)){
                    Toast.makeText(Signup.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password_s)){
                    Toast.makeText(Signup.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email_s, password_s).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Signup.this, "Account created", Toast.LENGTH_SHORT).show();
                                    userId= Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                    userdbref.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Data successfully saved
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // An error occurred while saving data
                                        }
                                    });

                                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Signup.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

    }
}