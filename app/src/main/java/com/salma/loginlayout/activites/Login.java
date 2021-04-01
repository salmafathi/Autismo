package com.salma.loginlayout.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salma.loginlayout.Interfaces.OnfetUserFavorites;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.Subcategory;
import com.salma.loginlayout.database.User;
import com.salma.loginlayout.networking.NetworkCheck;
import com.salma.loginlayout.sharedpreferences.SharedPreferenceManager;
import com.salma.loginlayout.validations.Validations;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;


public class Login extends AppCompatActivity {


    public static final int GOOGLE_SIGNIN_CODE = 9001;
    private SharedPreferenceManager preferenceManager;

    private static final String TAG = "EmailPassword";
    boolean loged_before = false ;


    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    DatabaseReference favoriteref;

    ArrayList<Subcategory> userFavoritesSubcats = new ArrayList<>();


    private EditText email;
    private EditText password ;
    private Button login ;
    private SignInButton gmailButton ;
    TextView signup ;


    NetworkCheck networkcheck ;
    boolean networkconnection;

    GoogleSignInOptions gso ;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount googleSigninAccount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // INaitialize fire base database root reference
        mDatabase = FirebaseDatabase.getInstance().getReference();



        //mail and password
         email =(EditText)findViewById(R.id.editTextTextEmailAddress);
         password = (EditText) findViewById(R.id.editTextTextPassword);
         signup = (TextView) findViewById(R.id.sign_up_textview);
         login = (Button)findViewById(R.id.loginabutton);
         gmailButton = (SignInButton) findViewById(R.id.Gmail);
         gmailButton.setSize(SignInButton.SIZE_STANDARD);


         setGooglePlusButtonText(gmailButton,"Log in with Google Account");

        // 1-check internet connectivity
        networkcheck = new NetworkCheck(getApplicationContext());
        networkconnection = networkcheck.isNetworkAvailable();


        //login Button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String myEmail = email.getText().toString();
                String myPassword = password.getText().toString();

                if(networkconnection){
                    // 2-check empty mail or password
                    Validations validation = new Validations(getApplicationContext());
                    if(!(validation.isEmptyMail(myEmail)||validation.isEmptyPassword(myPassword))){
                            // 3- check valid mail and password
                            if(validation.isValidEmail(myEmail)){
                                if(validation.isValidPassword(myPassword)){

                                        mAuth.signInWithEmailAndPassword(myEmail, myPassword)
                                                .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // save user data to shared preferences
                                                            FirebaseUser user = mAuth.getCurrentUser();
                                                            preferenceManager= new SharedPreferenceManager(Login.this,"userdata");
                                                            getuserfavorites(new OnfetUserFavorites() {
                                                                @Override
                                                                public void ongetuserfavorites(ArrayList<Subcategory> userfavorites) {
                                                                    User myuser = new User(user.getUid(),user.getDisplayName(), user.getEmail(), user.getProviderId(),user.getPhotoUrl().toString(), userfavorites);
                                                                    preferenceManager.setValue("data",myuser);
                                                                    // Sign in success, update UI with the signed-in user's information
                                                                    final Intent home = new Intent(Login.this, HomeMainCategories.class);
                                                                    startActivity(home);
                                                                }
                                                            });
                                                        }
                                                        else {
                                                            // If sign in fails, display a message to the user.
                                                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                                                            Toast.makeText(Login.this, "New mamber.Please Sign up !",
                                                                    Toast.LENGTH_SHORT).show();

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

        //Sign up button
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Intent sign = new Intent(Login.this,SignUp.class);
                startActivity(sign);
            }
        });


        //Gmail Login Button
         gmailButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if(networkconnection) {
                          gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                 .requestIdToken(getString(R.string.default_web_client_id))
                                 .requestEmail()
                                  .requestProfile()
                                 .build();

                         mGoogleSignInClient = GoogleSignIn.getClient(Login.this, gso);
                         googleSigninAccount = GoogleSignIn.getLastSignedInAccount(Login.this);


                         Intent googleSignintent = mGoogleSignInClient.getSignInIntent();
                         startActivityForResult(googleSignintent, GOOGLE_SIGNIN_CODE);


                 }
                 else{
                     Toast.makeText(getApplicationContext(),"Sorry! Internet Connection Error",Toast.LENGTH_LONG).show();
                 }
             }
         });
        
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
                                    //save data to shared pref
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    preferenceManager= new SharedPreferenceManager(Login.this,"userdata");
                                     getuserfavorites(new OnfetUserFavorites() {
                                         @Override
                                         public void ongetuserfavorites(ArrayList<Subcategory> userfavorites) {
                                             User myuser = new User(user.getUid(),user.getDisplayName(), user.getEmail(), user.getProviderId(), user.getPhotoUrl().toString(),userfavorites);
                                             preferenceManager.setValue("data",myuser);
                                             Log.d("looooooooo", "login : "+myuser.getName()+" , "+myuser.getUid());
                                             // Sign in success, update UI with the signed-in user's information
                                             final Intent home = new Intent(Login.this, HomeMainCategories.class);
                                             startActivity(home);
                                         }
                                     });


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
             Intent home = new Intent(Login.this, HomeMainCategories.class);
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


    public void getuserfavorites(OnfetUserFavorites callback) {
        //get user favorites
        favoriteref = mDatabase.child("favorites").child(mAuth.getCurrentUser().getUid());
        favoriteref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    userFavoritesSubcats.clear();
                    if(ds != null){
                        String key = ds.getKey();
                        Subcategory fav = snapshot.child(key).getValue(Subcategory.class);
                        userFavoritesSubcats.add(fav);
                    }
                }
                for(Subcategory favsubs : userFavoritesSubcats){
                    favsubs.setStar_state(1);
                }
                callback.ongetuserfavorites(userFavoritesSubcats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("llllllllllllll", "onCancelled: ");
            }

        });
    }
}