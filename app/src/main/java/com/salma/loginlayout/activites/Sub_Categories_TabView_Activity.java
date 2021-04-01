package com.salma.loginlayout.activites;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.salma.loginlayout.Adapters.Sub_Categories_PagerAdapter;
import com.salma.loginlayout.Interfaces.ClickonSubcat;
import com.salma.loginlayout.Interfaces.OnFragmentClickListener;
import com.salma.loginlayout.Interfaces.OnFragmentFavoritesClickListener;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.Category;
import com.salma.loginlayout.database.Subcategory;
import com.salma.loginlayout.database.Tab;
import com.salma.loginlayout.networking.NetworkCheck;
import com.salma.loginlayout.ui.fragments.Sub_Category_Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;

public class Sub_Categories_TabView_Activity extends AppCompatActivity implements OnFragmentClickListener, OnFragmentFavoritesClickListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    DatabaseReference favoriteref ;


    TabLayout tab_layout;
    ViewPager pager ;
    Sub_Categories_PagerAdapter pageAdapt ;

    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<ArrayList<Subcategory>> all_sub_categories = new ArrayList<>();
    String title ;
    ToggleButton star ;
    ProgressDialog progressDialog;
    NetworkCheck networkcheck = new NetworkCheck(this);

    Boolean done = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_categories);



        title = getIntent().getStringExtra("title");

        //Actionbar toolbar ---------------------------------------------
        Toolbar toolbar = findViewById(R.id.Hometoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });

        //firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);

        //inflate tabs and viewpager
        tab_layout = findViewById(R.id.tabs22);
        pager = findViewById(R.id.vb2);
        View v  = LayoutInflater.from(this).inflate(R.layout.sub_card_view, null, false);
        star = v.findViewById(R.id.favorate);

        //Get data from database
        categories = getIntent().getParcelableArrayListExtra("allcats");
        for (int i =0; i<categories.size();i++ ){
            all_sub_categories.add(getIntent().getParcelableArrayListExtra("subcat"+i));
        }

        //pager Adapter
         pageAdapt = new Sub_Categories_PagerAdapter(getSupportFragmentManager());

        for(int i = 0 ; i<all_sub_categories.size();i++){
             pageAdapt.addTab(new Tab(categories.get(i), Sub_Category_Fragment.newInstance(all_sub_categories.get(i))));
        }


        pager.setAdapter(pageAdapt);


        //connect tabs with pager
        tab_layout.setupWithViewPager(pager);


        //tabs listener
        tab_layout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //viewpager listener
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




        //floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent custom = new Intent(Sub_Categories_TabView_Activity.this,CustomSubCategories.class);
                startActivity(custom);
            }
        });

    }


    @Override
    public void OnFragmentInteraction(String subitemname, String uriImage) {
         ClickonSubcat obj = new ClickonSubcat();
         obj.GoToViewModelAfterClickOnSubcat(this,subitemname,uriImage);
    }


    @Override
    public void onitemFragmentCheckStar(int id, String sub_category_name, String sub_category_image,int category_id, int star_state, int privacy) {
        Subcategory fav = new Subcategory(id, sub_category_name, sub_category_image, category_id, star_state, privacy);
        favoriteref = mDatabase.child("favorites").child(mAuth.getCurrentUser().getUid());
        Log.d("dooooooooooo", "onitemFragmentCheckStar: "+favoriteref);
        if (networkcheck.isNetworkAvailable()) {
                favoriteref.push().setValue(fav).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(getCurrentFocus(),  sub_category_name+ " added to favorites", Snackbar.LENGTH_SHORT)
                                .setAnchorView(R.id.fab)
                                .setAction("Favorites", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent in = new Intent(Sub_Categories_TabView_Activity.this  , HomeMainCategories.class);
                                        startActivity(in);
                                    }
                                })
                                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary))
                                .show();
                    }
                }) ;
                    fav.setStar_state(1);


        }
        else{
            Snackbar.make(getCurrentFocus(), "Sorry, No Internet connection", Snackbar.LENGTH_SHORT).setAnchorView(R.id.fab).show();

        }

    }


    @Override
    public void onitemFragmentUncheckStar(int id, String sub_category_name, String sub_category_image,int category_id, int star_state, int privacy) {
        favoriteref = mDatabase.child("favorites").child(mAuth.getCurrentUser().getUid());
        Query favkeyref = favoriteref.orderByChild("sub_category_name").equalTo(sub_category_name);
        if(networkcheck.isNetworkAvailable()) {
            try {
                favkeyref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        snapshot.getRef().setValue(null);
                        Snackbar.make(Objects.requireNonNull(getCurrentFocus()), sub_category_name + " Removed from favorites", Snackbar.LENGTH_SHORT)
                                .setAnchorView(R.id.fab)
                                .setAction("Favorites", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent in = new Intent(Sub_Categories_TabView_Activity.this, HomeMainCategories.class);
                                        startActivity(in);
                                    }
                                })
                                .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primary))
                                .show();


                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
                Log.d("doooooooooooooo", "onitemFragmentUncheckStar: "+e);
            }
       }
       else{
           Snackbar.make(getCurrentFocus(), " No Internet Connection", Snackbar.LENGTH_SHORT).setAnchorView(R.id.fab).show();

       }
    }

}