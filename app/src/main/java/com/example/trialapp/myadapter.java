package com.example.trialapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<Item,myadapter.myviewholder> {

    public myadapter(@NonNull FirebaseRecyclerOptions<Item> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Item model) {
        holder.productname.setText(model.getName());
        holder.productdescription.setText(model.getDescription());
        holder.productprice.setText(model.getPrice());
        Glide.with(holder.img1.getContext()).load(model.getUrlimg()).into(holder.img1);

            holder.img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppCompatActivity activity=(AppCompatActivity)view.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new descFragment(model.getUrlimg(),model.getName(),model.getDescription(),model.getPrice())).addToBackStack(null).commit();

                }
            });

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{

        ImageView img1;
        TextView productname,productdescription,productprice;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            img1=itemView.findViewById(R.id.img1);
            productname=itemView.findViewById(R.id.productname);
            productdescription=itemView.findViewById(R.id.productdescription);
            productprice=itemView.findViewById(R.id.productprice);
        }
    }

}
