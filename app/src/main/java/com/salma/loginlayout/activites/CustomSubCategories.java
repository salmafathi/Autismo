package com.salma.loginlayout.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salma.loginlayout.Interfaces.OnCustomFragmentAddListener;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.DatabaseAcess;
import com.salma.loginlayout.database.Subcategory;
import com.salma.loginlayout.ui.fragments.Custom_SubCategory_Fragment;
import com.salma.loginlayout.ui.fragments.HomeMainCatFragment;

import java.util.ArrayList;

public class CustomSubCategories extends AppCompatActivity implements OnCustomFragmentAddListener {

    DatabaseAcess db ;
    ArrayList<Subcategory> allcustomsubcats_from_db = new ArrayList<>();
    ArrayList<Subcategory> usercustomsubcats_from_firebase = new ArrayList<>();
    ArrayList<Subcategory> search_list = new ArrayList<>();
    SearchView search ;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth ;
    DatabaseReference customsubref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_sub_categories);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        //Actionbar toolbar ---------------------------------------------
        Toolbar toolbar = findViewById(R.id.Cutom);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                onBackPressed();
            }
        });



        //Get data from database
        db = DatabaseAcess.getInstance(this);
        db.open();
        allcustomsubcats_from_db = db.get_all_custom_subcats();
        db.close();

        //firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        customsubref = mDatabase.child("customsubcats").child(mAuth.getCurrentUser().getUid());

        //search View
        search = findViewById(R.id.customsearch);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                newText = newText.toLowerCase();
                search_list.clear();
                if(newText.isEmpty()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.customcategoryfragment, Custom_SubCategory_Fragment.newInstance(allcustomsubcats_from_db)).commit();
                }
                else{
                    for(Subcategory customesubcat : allcustomsubcats_from_db){
                        if(customesubcat.getSub_category_name().toLowerCase().contains(newText)) {
                            search_list.add(customesubcat);
                        }
                        }
                    getSupportFragmentManager().beginTransaction().replace(R.id.customcategoryfragment, Custom_SubCategory_Fragment.newInstance(search_list)).commit();
                }
                return false;
            }
        });


    }

    @Override
    public void CustomFragmentAddListener(int customSubCat_id, String customName, String CustomImage,int CustomCategory, int customStarState, int customPrivacy) {
        // add custom to firebase
        Subcategory customsubcat = new Subcategory( customSubCat_id,  customName,  CustomImage,CustomCategory, customStarState,  customPrivacy);
        customsubref.push().setValue(customsubcat);


        // get data from firebase
//        customsubref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                usercustomsubcats_from_firebase.clear();
//                for(DataSnapshot ds : snapshot.getChildren())
//                {
//                    if(ds != null){
//                        String key = ds.getKey();
//                        Subcategory custom = snapshot.child(key).getValue(Subcategory.class);
//                        usercustomsubcats_from_firebase.add(custom);
//                    }
//                }
//
//                //////////////////compare customs from firebase with scustoms from database
//                for (int i =0; i<allcustomsubcats_from_db.size();i++ ){
//                    for (Subcategory subcat : usercustomsubcats_from_firebase) {
//                        if (subcat.getId() == allcustomsubcats_from_db.get(i).getId()) {
//                            allcustomsubcats_from_db.remove(i);
//                        }
//                    }
//                }
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.customcategoryfragment, Custom_SubCategory_Fragment.newInstance(allcustomsubcats_from_db)).commit();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        Intent in = new Intent(this,HomeMainCategories.class);
        startActivity(in);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // get data from firebase
        customsubref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usercustomsubcats_from_firebase.clear();
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    if(ds != null){
                        String key = ds.getKey();
                        Subcategory custom = snapshot.child(key).getValue(Subcategory.class);
                        usercustomsubcats_from_firebase.add(custom);
                    }
                }

                //////////////////compare customs from firebase with scustoms from database
                for (int i =0; i<allcustomsubcats_from_db.size();i++ ){
                    for (Subcategory subcat : usercustomsubcats_from_firebase) {
                        if (subcat.getId() == allcustomsubcats_from_db.get(i).getId()) {
                            allcustomsubcats_from_db.remove(i);
                        }
                    }
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.customcategoryfragment, Custom_SubCategory_Fragment.newInstance(allcustomsubcats_from_db)).commit();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}