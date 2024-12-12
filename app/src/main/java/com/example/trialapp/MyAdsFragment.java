package com.example.trialapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trialapp.adsadapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MyAdsFragment extends Fragment {


    RecyclerView rv;
    FirebaseAuth auth;
    adsadapter adapter;
    private static String phoneNumber;

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public  void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_myads, container, false);

        rv=(RecyclerView)v.findViewById(R.id.rcview1);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(layoutManager);

        auth = FirebaseAuth.getInstance();

        String uid = Objects.requireNonNull(auth.getCurrentUser()).getUid();


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    phoneNumber = dataSnapshot.child("phonenumber").getValue(String.class);
                }
            }
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"error in loading", Toast.LENGTH_SHORT).show();
            }
        });
        FirebaseRecyclerOptions<Item> options = new FirebaseRecyclerOptions.Builder<Item>().setQuery(FirebaseDatabase.getInstance().getReference().child("items").orderByChild("number").equalTo(phoneNumber), Item.class).build();

        adapter=new adsadapter(options);
        adapter.startListening();
        rv.setAdapter(adapter);

        return v;

    }

}