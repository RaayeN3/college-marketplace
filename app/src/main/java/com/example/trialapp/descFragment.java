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

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String url,name,descr,price, phonenum;
    Button call;

    public descFragment() {

    }
    public descFragment(String url,String name,String descr,String price,String phonenum) {
        this.url=url;
        this.name=name;
        this.descr=descr;
        this.price=price;
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vi= inflater.inflate(R.layout.fragment_desc, container, false);
        ImageView imageholder=vi.findViewById(R.id.imagegholder);
        TextView nameholder=vi.findViewById(R.id.nameholder);
        TextView descriptionholder=vi.findViewById(R.id.descriptionholder);
        TextView priceholder=vi.findViewById(R.id.priceholder);

        call = vi.findViewById(R.id.call_button);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL); intent.setData(Uri.parse("tel:" + phonenum)); startActivity(intent);
            }
        });

        nameholder.setText(name);
        descriptionholder.setText(descr);
        priceholder.setText(price);
        Glide.with(getContext()).load(url).into(imageholder);
        return vi;
    }

    public void onBackPressed(){

        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).addToBackStack(null).commit();

    }
}