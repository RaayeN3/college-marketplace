package com.example.trialapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Item_Details extends AppCompatActivity {

    EditText prod_name,prod_desc,prod_price;
    ImageButton save_item;
    ImageView img_up;
    String imgurl;
    FirebaseAuth auth;

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
        auth = FirebaseAuth.getInstance();

        itemdbref= FirebaseDatabase.getInstance().getReference().child("items");

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            assert data != null;
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
    public void saveData() {
        if (uri == null) {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Get the current user's UID from Firebase Authentication
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Get the image name from the URI (or default name if not available)
            String imageName = getImageNameFromUri(uri);

            // Combine the user ID and image name to create a unique file name
            String fileName = userId + "_" + imageName;

            // Create the file in the internal storage
            File file = new File(getFilesDir(), fileName);

            // Open input and output streams
            InputStream inputStream = getContentResolver().openInputStream(uri);
            OutputStream outputStream = Files.newOutputStream(file.toPath());

            byte[] buffer = new byte[1024];
            int bytesRead;

            // Copy bytes from input stream to output stream
            while (true) {
                assert inputStream != null;
                if ((bytesRead = inputStream.read(buffer)) == -1) break;
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close streams
            inputStream.close();
            outputStream.close();

            // Update imgurl with the file path for future use
            imgurl = file.getAbsolutePath();

            Toast.makeText(this, "Image saved locally: " + imgurl, Toast.LENGTH_SHORT).show();

            // Proceed with uploading data or any further logic
            uploadData();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getImageNameFromUri(Uri uri) {
        // Try to extract the display name from the content resolver
        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1 && cursor.moveToFirst()) {
                        return cursor.getString(nameIndex);
                    }
                } finally {
                    ((Cursor) cursor).close();
                }
            }
        }

        // Fallback to extracting the last path segment
        if (uri.getPath() != null) {
            return new File(uri.getPath()).getName();
        }

        // Default name if all else fails
        return "default_image_name";
    }

   void add_item(){
       String item_n=prod_name.getText().toString();
        String item_d=prod_desc.getText().toString();
        String item_p=prod_price.getText().toString();
        if(item_n.isEmpty()){
            prod_name.setError("Product name required");
            return;
        }
       if(item_p.isEmpty()){
            prod_name.setError("Product price required");
            return;
        }

        Item item=new Item();
        item.setName(item_n);
        item.setDescription(item_d);
        item.setPrice(item_p);

        itemdbref.push().setValue(item);
       Toast.makeText(Item_Details.this,"Item added",Toast.LENGTH_SHORT).show();}
public void uploadData(){
    String item_n=prod_name.getText().toString();
    String item_d=prod_desc.getText().toString();
    String item_p=prod_price.getText().toString();
    String get_uid= Objects.requireNonNull(auth.getCurrentUser()).getUid();
    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
    usersRef.child(get_uid).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                String phoneNumber = dataSnapshot.child("phonenumber").getValue(String.class);
                Item item=new Item( item_n, item_d,item_p,imgurl,phoneNumber);

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
                        Toast.makeText(Item_Details.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),"error in loading", Toast.LENGTH_SHORT).show();
        }
    });





    String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());


    }
}