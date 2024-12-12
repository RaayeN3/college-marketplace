package com.example.trialapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

public class descFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String url, name, descr, price, phonenum;
    private Button call, plus, minus;
    private TextView quantityTextView;
    private int quantity = 0; // Initial quantity

    public descFragment() {
    }

    public descFragment(String url, String name, String descr, String price, String phonenum) {
        this.url = url;
        this.name = name;
        this.descr = descr;
        this.price = price;
        this.phonenum = phonenum;
    }

    public static descFragment newInstance(String param1, String param2) {
        descFragment fragment = new descFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vi = inflater.inflate(R.layout.fragment_desc, container, false);
        ImageView imageholder = vi.findViewById(R.id.imagegholder);
        TextView nameholder = vi.findViewById(R.id.nameholder);
        TextView descriptionholder = vi.findViewById(R.id.descriptionholder);
        TextView priceholder = vi.findViewById(R.id.priceholder);
        quantityTextView = vi.findViewById(R.id.textView);

        call = vi.findViewById(R.id.call_button);
        plus = vi.findViewById(R.id.plus);
        minus = vi.findViewById(R.id.minus);

        // Set initial quantity
        quantityTextView.setText(String.valueOf(quantity));

        // Call button functionality
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phonenum));
                startActivity(intent);
            }
        });

        // Plus button functionality
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityTextView.setText(String.valueOf(quantity));
            }
        });

        // Minus button functionality
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 0) { // Ensure quantity doesn't go below 0
                    quantity--;
                }
                quantityTextView.setText(String.valueOf(quantity));
            }
        });

        // Set data for views
        nameholder.setText(name);
        descriptionholder.setText(descr);
        priceholder.setText(price);
        Glide.with(requireContext()).load(url).into(imageholder);
        return vi;
    }

    public void onBackPressed() {
        AppCompatActivity activity = (AppCompatActivity) getContext();
        assert activity != null;
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).addToBackStack(null).commit();
    }
}
