package com.example.trialapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class AccountFragment extends Fragment {

    FirebaseAuth auth;
    Button button;
    //    TextView textView;
    FirebaseUser user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_account, container, false);

        auth = FirebaseAuth.getInstance();
        button = v.findViewById(R.id.logout);


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