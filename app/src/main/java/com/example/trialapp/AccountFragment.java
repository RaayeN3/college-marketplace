package com.example.trialapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AccountFragment extends Fragment {

    private TextView name, phone;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_account, container, false);

        // Initialize Firebase Auth and UI components
        FirebaseAuth auth = FirebaseAuth.getInstance();
        Button button = v.findViewById(R.id.logout);
        name = v.findViewById(R.id.name);
        phone = v.findViewById(R.id.phone);

        // Fetch current user's UID
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String uid = user.getUid();

            // Reference to the "users" node in the database
            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String namefb = dataSnapshot.child("fullname").getValue(String.class);
                        String phoneNumber = dataSnapshot.child("phonenumber").getValue(String.class);

                        // Update the UI with fetched data
                        name.setText("Name: " + (namefb != null ? namefb : "N/A"));
                        phone.setText("Phone Number: " + (phoneNumber != null ? phoneNumber : "N/A"));
                    } else {
                        //si les donnees utilisateurs sont introuvables
                        Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Error loading data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //loqique pour deconnecter l'utilisateur
        button.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getActivity(), Login.class);
            startActivity(intent);
            requireActivity().finish(); // Ensure the current activity is removed from the back stack
        });

        return v;
    }
}
