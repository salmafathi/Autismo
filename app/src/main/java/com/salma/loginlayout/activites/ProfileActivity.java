package com.salma.loginlayout.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.User;
import com.salma.loginlayout.sharedpreferences.SharedPreferenceManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity{

    TextView userName ;
    ImageView userImage ;
    EditText personName ;
    EditText personEmail ;
    LinearLayout signout ;


    private FirebaseAuth mAuth ;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //get user data from shared preference
        SharedPreferenceManager preferenceManager= new SharedPreferenceManager(this,"userdata");
        User shared_user =  preferenceManager.getValue("data");
        Log.d("looooooooooo", "onCreate: profile , get data from shared : "+shared_user.getUid()+" , "+shared_user.getName());

        // toolbar ---------------------------------------------------
        Toolbar toolbar = findViewById(R.id.profiletoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        //firebase Auth-----------------------------------------------------------
        mAuth = FirebaseAuth.getInstance();

        //inflate components -----------------------------------------------------
        userName = findViewById(R.id.uName);
        userImage = findViewById(R.id.uImage);
        personName = findViewById(R.id.editTextTextPersonName2);
        personEmail = findViewById(R.id.editTextTextEmailAddress2);
        signout = findViewById(R.id.sout);

        //set Component ---------------------------------------------------------

                userName.setText(shared_user.getName());
                personName.setText(shared_user.getName());
                personEmail.setText(shared_user.getEmail());
                Picasso.get().load(shared_user.getImageurl().trim().replace("s96-c", "s500-c").trim()).into(userImage);




        //edit user name ---------------------------------------------------------------
        personName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                personName.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_checked_symbol),null);
            }

            @Override
            public void afterTextChanged(Editable s) {

                personName.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        if(event.getAction() == MotionEvent.ACTION_UP) {
                            if (event.getRawX() >= (personName.getRight() - personName.getCompoundDrawables()[2].getBounds().width() * 2)) {
                                personName.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_checked_off), null);
                                userName.setText(personName.getText().toString());

                                //update user info in  the server :

                                        FirebaseUser user =   mAuth.getCurrentUser();
                                        if(user!= null && user.getUid().equals(shared_user.getUid())) {
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(personName.getText().toString())
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                shared_user.setName(personName.getText().toString());
                                                                preferenceManager.updateValue("data",shared_user);
                                                                Log.d("looooooooooooo", "onComplete: update name : "+shared_user.getName()+" , "+shared_user.getUid());
                                                                Snackbar.make(v, "Your Name updated to : " + personName.getText().toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                            } else {
                                                                Log.e("UPDATEPROFILE", task.getException().getMessage());
                                                            }
                                                        }
                                                    });
                                        }






                                return true;
                            }
                        }
                        return false;
                    }
                });

            }
        });


        //edit user Email --------------------------------------------------------------
//        personEmail.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                personEmail.setCompoundDrawablesWithIntrinsicBounds(null,null,getDrawable(R.drawable.ic_checked_symbol),null);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                personEmail.setOnTouchListener(new View.OnTouchListener() {
//                    @Override
//                    public boolean onTouch(View v, MotionEvent event) {
//
//                        if(event.getAction() == MotionEvent.ACTION_UP) {
//                            if (event.getRawX() >= (personEmail.getRight() - personEmail.getCompoundDrawables()[2].getBounds().width() * 2)) {
//                                personEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, getDrawable(R.drawable.ic_checked_off), null);
//
//                                //update user info in  the server :
//                                mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//                                    @Override
//                                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                                        FirebaseUser user =   firebaseAuth.getCurrentUser();
//                                        if(user!= null) {
//                                            AuthCredential credential = EmailAuthProvider.getCredential("user@example.com", "password1234");
//                                            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            user.updateEmail("user@example.com").addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                        @Override
//                                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                                            if (task.isSuccessful()) {
//                                                                                Snackbar.make(v, "Your Email updated to : " + personEmail.getText().toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                                                                            }
//                                                                            else {
//                                                                                Log.e("UPDATEPROFILE", task.getException().getMessage());
//                                                                            }
//                                                                        }
//                                                                    });
//                                                        }
//                                                    });
//
//                                        }
//
//
//                                    }
//                                });
//
//
//                                return true;
//                            }
//                        }
//                        return false;
//                    }
//                });
//            }
//        });


        //Sign out Function ------------------------------------------------------------

        //sign out function
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                GoogleSignIn.getClient(ProfileActivity.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                SharedPreferenceManager preferenceManager = new SharedPreferenceManager(ProfileActivity.this,"userdata");
                                preferenceManager.clear();
                                Log.d("signoutError", "noerror " );
                                Intent in = new Intent(ProfileActivity.this, Login.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK); // To clean up all activities
                                startActivity(in);
                                finish();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("signoutError", "onFailure: " + e);
                            }
                        });
            }
        });

    }

}