package com.salma.loginlayout.activites;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.Category;
import com.salma.loginlayout.database.DatabaseAcess;
import com.salma.loginlayout.database.Subcategory;


import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class AddNewSubcategory extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    public static final int PICK_IMAGE = 1;
    public static final int PICK_VOICE = 2;

    private Uri filePath;
    Spinner spiner ;
    ImageView selectedImage ;
    EditText itemName ;
    EditText editTextTextmp3link;
    Button submitbuttton;


    String categoryname;
    String itemname ;
    String imageuri;
    String voiceurl;
    int catid;

    Uri uri;
    ArrayList<Category> cats = new ArrayList<Category>();
    ArrayList<String> catsnames = new ArrayList<String>();
    FloatingActionButton uploadImage ;
    FloatingActionButton uploadVoice ;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference uploads;
    private StorageReference storagereference;
    private StorageReference uploadfolderreference;
    DatabaseAcess db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_subcategory);

        //get data -----------------------------------

        // 1- get categories data
        cats = getIntent().getParcelableArrayListExtra("categories");
        for(Category cat :cats){
            catsnames.add(cat.getCategory_name());
        }


        //Inflate components --------------------------
        spiner = findViewById(R.id.spinner2);
        itemName = findViewById(R.id.itemName);
        selectedImage = findViewById(R.id.selectedImage);
        uploadImage = findViewById(R.id.floatinguploadImage);
        uploadVoice = findViewById(R.id.newvoiceButton);
        editTextTextmp3link = findViewById(R.id.editTextTextmp3link);
        editTextTextmp3link.setVisibility(View.INVISIBLE);
        submitbuttton = findViewById(R.id.submitbuttton);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storagereference = FirebaseStorage.getInstance().getReference();


        uploads = mDatabase.child("uploads").child(mAuth.getCurrentUser().getUid());
        uploadfolderreference = storagereference.child("uploads/"+ UUID.randomUUID().toString());
        StorageReference watermelonImagesRef = storagereference.child("uploads/watermelon.png");

        //set data-------------------------------------
        //set spiner data
        spiner.setOnItemSelectedListener(this);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catsnames);
        spiner.setAdapter(dataAdapter);

        //itemName listener
        itemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
               itemname = s.toString();
            }
        });

        //upload image listener
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open gallery and choose one image
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            }
        });

        //upload voice listener
        uploadVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,PICK_VOICE);
            }
        });

        //Submit button
        submitbuttton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spiner.getSelectedItem()!=null && itemname!=null && imageuri !=null && editTextTextmp3link.getText()!=null){
                    //upload image to filrebase
                    imageuri = uploadImage();


                    //save data in database
                    for(Category cat :cats){
                        if(cat.getCategory_name().equals(categoryname)){
                            catid = cat.getCategory_id();
                        }
                    }
                    Subcategory data = new Subcategory(itemname,imageuri,catid,1);
                    db = DatabaseAcess.getInstance(AddNewSubcategory.this);
                    db.open();
                    db.insert_subcategory(data);

                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         categoryname = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //after choose an image/voice from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                uploadImage.setVisibility(View.INVISIBLE);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                selectedImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        else if(requestCode == PICK_VOICE && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            uploadVoice.setVisibility(View.INVISIBLE);
            editTextTextmp3link.setVisibility(View.VISIBLE);
            editTextTextmp3link.setText(uri.toString());
            voiceurl = uri.toString();
        }

    }

    private String uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            uploadfolderreference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                             uploadfolderreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                 @Override
                                 public void onSuccess(Uri uri) {
                                     imageuri = uri.toString();
                                 }
                             });
                            Toast.makeText(AddNewSubcategory.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddNewSubcategory.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

        return imageuri;
    }
}