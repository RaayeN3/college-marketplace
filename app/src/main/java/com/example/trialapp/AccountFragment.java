package com.example.trialapp;

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


public class AccountFragment extends Fragment {

    FirebaseAuth auth;

    TextView name, phone;
    Button button;
    //    TextView textView;
    FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        auth = FirebaseAuth.getInstance();
        button = v.findViewById(R.id.logout);

        name =v.findViewById(R.id.name);
        phone = v.findViewById(R.id.phone);

        String uid =auth.getCurrentUser().getUid();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String namefb = dataSnapshot.child("fullname").getValue(String.class);
                    String phoneNumber = dataSnapshot.child("phonenumber").getValue(String.class);
                    name.setText("Name : "+namefb);
                    phone.setText("Phone Number : "+phoneNumber);
                }
            }
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"error in loading", Toast.LENGTH_SHORT);
            }
        });




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user = auth.getCurrentUser();
                if (user == null) {
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);

                } else {
//            textView.setText(user.getEmail());
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);
                    getActivity().finish();

                }
            }
        });

        return v;
    }
            }

//        );

        // Inflate the layout for this fragment
//        return v;
//    }
//        }