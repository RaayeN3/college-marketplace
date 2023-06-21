package com.example.trialapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Item_Details extends AppCompatActivity {

    EditText prod_name,prod_desc,prod_price;
    ImageButton save_item;
    //ImageView img_up;

//    ProgressBar prog;
    DatabaseReference itemdbref;

//    StorageReference img_ref;
//
//    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);


//        img_up = findViewById(R.id.item_img);
        prod_name=findViewById(R.id.item_name);
        prod_desc=findViewById(R.id.item_desc);
        prod_price=findViewById(R.id.item_price);
        save_item=findViewById(R.id.save_btn);


        itemdbref= FirebaseDatabase.getInstance().getReference().child("items");

//        img_ref = FirebaseStorage.getInstance().getReference();
        save_item.setOnClickListener( (v)->add_item());

//        img_up.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent galleryIntent = new Intent();
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent,2);
//            }
//        });


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode ==2 && resultCode == RESULT_OK && data != null){
//
//            imageUri = data.getData();
//            img_up.setImageURI(imageUri);
//
//        }
//    }

    void add_item(){
        String item_n=prod_name.getText().toString();
        String item_d=prod_desc.getText().toString();
        String item_p=prod_price.getText().toString();

        if(item_n==null||item_n.isEmpty()){
            prod_name.setError("Product name required");
            return;
        }
        if(item_p==null||item_p.isEmpty()){
            prod_name.setError("Product price required");
            return;
        }

        Item item=new Item();
        item.setName(item_n);
        item.setDescription(item_d);
        item.setPrice(item_p);

        itemdbref.push().setValue(item);
        Toast.makeText(Item_Details.this,"Item added",Toast.LENGTH_SHORT).show();

    }
}