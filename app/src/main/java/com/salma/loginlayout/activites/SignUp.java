package com.salma.loginlayout.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.salma.loginlayout.BuildConfig;
import com.salma.loginlayout.Interfaces.OnfetUserFavorites;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.Subcategory;
import com.salma.loginlayout.database.User;
import com.salma.loginlayout.networking.NetworkCheck;
import com.salma.loginlayout.sharedpreferences.SharedPreferenceManager;
import com.salma.loginlayout.validations.Validations;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;


public class SignUp extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";
    private DatabaseReference mDatabase;

    private EditText name;
    private EditText email ;
    private EditText password ;
    Button signup ;
    SignInButton signupWithGmail ;
    TextView login_text_view ;

    NetworkCheck networkcheck ;
    boolean networkconnection ;

    GoogleSignInOptions gso ;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount googleSigninAccount ;
    public static final int GOOGLE_SIGNIN_CODE = 9001;

    private SharedPreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

       // INaitialize fire base database root reference
        mDatabase = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_sign_up);

         name = (EditText) findViewById(R.id.editTextTextPersonName);
         email =(EditText)findViewById(R.id.editTextTextEmailAddress3);
         password = (EditText) findViewById(R.id.editTextNumberPassword);
         signup = (Button)findViewById(R.id.sign_up);
         signupWithGmail = (SignInButton)findViewById(R.id.GmailSignup);
         login_text_view = (TextView)findViewById(R.id.login_text_view);
         setGooglePlusButtonText(signupWithGmail,"Sign up with Google");


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myEmail = email.getText().toString();
                String myPassword = password.getText().toString();
                final String myname = name.getText().toString();
                //internet check
                networkcheck = new NetworkCheck(getApplicationContext());
                networkconnection = networkcheck.isNetworkAvailable();

                if(networkconnection){
                    // 2-check empty mail or password
                    Validations validation = new Validations(getApplicationContext());
                    if(!(validation.isEmptyMail(myEmail)||validation.isEmptyPassword(myPassword)||validation.isEmptyName(myname))){
                        // 3- check valid mail and password
                        if(validation.isValidEmail(myEmail)){
                            if(validation.isValidPassword(myPassword)){

                                //create user account ---------------------------------
                                mAuth.createUserWithEmailAndPassword(myEmail, myPassword).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    // add username and photo to user account ----------
                                                    FirebaseUser user = mAuth.getCurrentUser();
                                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(myname)
                                                            .setPhotoUri(getURLForResource(R.drawable.person))
                                                            .build();

                                                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        //save user info to shared preference
                                                                        FirebaseUser user = mAuth.getCurrentUser();
                                                                        preferenceManager= new SharedPreferenceManager(SignUp.this,"userdata");
                                                                        ArrayList<Subcategory> userfavorites = new ArrayList<>();
                                                                        User myuser = new User(user.getUid(),user.getDisplayName(), user.getEmail(), user.getProviderId(),user.getPhotoUrl().toString(), userfavorites);
                                                                        preferenceManager.setValue("data",myuser);
                                                                        // Sign in success, update UI with the signed-in user's information
                                                                        final Intent home = new Intent(SignUp.this, HomeMainCategories.class);
                                                                        startActivity(home);
                                                                    }
                                                                    else{
                                                                        Toast.makeText(SignUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });


                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Password must be grater than 6 ",Toast.LENGTH_LONG).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Not Valid Email",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Sorry! Empty Fields!",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Sorry! Internet Connection Error",Toast.LENGTH_LONG).show();
                }
            }
        });


        signupWithGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(networkconnection) {
                    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .requestProfile()
                            .build();

                    mGoogleSignInClient = GoogleSignIn.getClient(SignUp.this, gso);
                    googleSigninAccount = GoogleSignIn.getLastSignedInAccount(SignUp.this);


                    Intent googleSignintent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(googleSignintent, GOOGLE_SIGNIN_CODE);


                }
                else{
                    Toast.makeText(getApplicationContext(),"Sorry! Internet Connection Error",Toast.LENGTH_LONG).show();
                }
            }
        });

        login_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent login = new Intent(SignUp.this,Login.class);
                startActivity(login);
            }
        });


    }

    private Uri getURLForResource(int resourceId) {
        return Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID +"/" +resourceId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGNIN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String idToken = account.getIdToken();

                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //save user info to shared preference
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    preferenceManager= new SharedPreferenceManager(SignUp.this,"userdata");
                                    ArrayList<Subcategory> userfavorites = new ArrayList<>();
                                    User myuser = new User(user.getUid(),user.getDisplayName(), user.getEmail(), user.getProviderId(),user.getPhotoUrl().toString() ,userfavorites);
                                    preferenceManager.setValue("data",myuser);
                                    // Sign in success, update UI with the signed-in user's information
                                    final Intent home = new Intent(SignUp.this, Sub_Categories_TabView_Activity.class);
                                    startActivity(home);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithCredential:failure", task.getException());

                                }
                            }
                        });

            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(googleSigninAccount != null || mAuth.getCurrentUser() != null ){
            Intent home = new Intent(SignUp.this, HomeMainCategories.class);
            startActivity(home);
        }
    }

    protected void setGooglePlusButtonText(SignInButton signInButton, String buttonText) {

        // Find the TextView that is inside of the SignInButton and set its text
        for (int i = 0; i < signInButton.getChildCount(); i++) {
            View v = signInButton.getChildAt(i);
            if (v instanceof TextView) {
                TextView tv = (TextView) v;
                tv.setText(buttonText);
                return;
            }
        }
    }
}