package com.example.trialapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.FirebaseDatabase;


public class HomeFragment extends Fragment {


    //Button add_ele;
    FloatingActionButton add_item_btn;
    RecyclerView rv;
    myadapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vi = inflater.inflate(R.layout.fragment_home, container, false);
        add_item_btn= vi.findViewById(R.id.add_item);
        add_item_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Item_Details.class);
                startActivity(intent);
            }
        });

        rv=(RecyclerView)vi.findViewById(R.id.rcview);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<Item> options =
                new FirebaseRecyclerOptions.Builder<Item>().setQuery(FirebaseDatabase.getInstance().getReference().child("items"), Item.class).build();

        adapter=new myadapter(options);
        rv.setAdapter(adapter);


        return vi;
    }
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
}