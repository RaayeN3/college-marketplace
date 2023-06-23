package com.example.trialapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Item_Details extends AppCompatActivity {

    EditText prod_name,prod_desc,prod_price;
    ImageButton save_item;
    ImageView img_up;
    String imgurl;

//    ProgressBar prog;
    DatabaseReference itemdbref;

//    StorageReference img_ref;
//
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_details);


        img_up = findViewById(R.id.item_img);
        prod_name=findViewById(R.id.item_name);
        prod_desc=findViewById(R.id.item_desc);
        prod_price=findViewById(R.id.item_price);
        save_item=findViewById(R.id.save_btn);


        itemdbref= FirebaseDatabase.getInstance().getReference().child("items");

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            img_up.setImageURI(uri);
                        } else {
                            Toast.makeText(Item_Details.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        //save_item.setOnClickListener( (v)->add_item());
        img_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        save_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });
    }
    public void saveData(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Android Images")
                .child(uri.getLastPathSegment());
        AlertDialog.Builder builder = new AlertDialog.Builder(Item_Details.this);
        builder.setCancelable(false);
        //builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imgurl = urlImage.toString();
                uploadData();
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
            }
        });
    }

//

//    void add_item(){
//        String item_n=prod_name.getText().toString();
//        String item_d=prod_desc.getText().toString();
//        String item_p=prod_price.getText().toString();
//
//        if(item_n==null||item_n.isEmpty()){
//            prod_name.setError("Product name required");
//            return;
//        }
//        if(item_p==null||item_p.isEmpty()){
//            prod_name.setError("Product price required");
//            return;
//        }
//
//        Item item=new Item();
//        item.setName(item_n);
//        item.setDescription(item_d);
//        item.setPrice(item_p);
//
//        itemdbref.push().setValue(item);
//        Toast.makeText(Item_Details.this,"Item added",Toast.LENGTH_SHORT).show();
public void uploadData(){
    String item_n=prod_name.getText().toString();
    String item_d=prod_desc.getText().toString();
    String item_p=prod_price.getText().toString();

    Item item=new Item( item_n, item_d,item_p,imgurl);
//    String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
    itemdbref.push().setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Item_Details.this, "Saved", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Item_Details.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });

    }
}