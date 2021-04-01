package com.salma.loginlayout.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.salma.loginlayout.Interfaces.ClickonSubcat;
import com.salma.loginlayout.Interfaces.OnFragmentClickListener;
import com.salma.loginlayout.Interfaces.OnFragmentFavoritesClickListener;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.Category;
import com.salma.loginlayout.database.DatabaseAcess;
import com.salma.loginlayout.database.Main_Categories;
import com.salma.loginlayout.database.Subcategory;
import com.salma.loginlayout.database.User;
import com.salma.loginlayout.networking.NetworkCheck;
import com.salma.loginlayout.sharedpreferences.SharedPreferenceManager;
import com.salma.loginlayout.ui.fragments.ErrorFragment;
import com.salma.loginlayout.ui.fragments.HomeMainCatFragment;
import com.salma.loginlayout.ui.fragments.Sub_Category_Fragment;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Objects;

public class HomeMainCategories extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeMainCatFragment.OnMainCategoryClickListener , OnFragmentClickListener, OnFragmentFavoritesClickListener {

    AppBarLayout appBarLayout ;
    DrawerLayout drawer;
    Toolbar toolbar ;

    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    View header_view;
    TextView profile ;
    TextView username;
    ImageView userImage;
    TextView mTitle;

    MenuItem signoutmenueItem ;
    Menu menuee;
    SubMenu favmenu;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth ;
    DatabaseReference favoriteref;
    DatabaseReference customsubref;

    SearchView search ;

    DatabaseAcess db ;
    ArrayList<Main_Categories>main_categories;
    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<Subcategory> subcats = new ArrayList<>();
    ArrayList<Subcategory> userFavoritesSubcats = new ArrayList<>();
    ArrayList<Subcategory> usercustomsubcats_from_firebase = new ArrayList<>();

    BottomNavigationView bottomnavigationview;
    BadgeDrawable badger;

    ArrayList<Subcategory> all_subcats = new ArrayList<>();
    ArrayList<Subcategory> search_list = new ArrayList<>();
    String intentee = "normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_main_categories);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        //Actionbar toolbar
        toolbar = findViewById(R.id.anim_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);




        //firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        //get user favorites
        favoriteref = mDatabase.child("favorites").child(mAuth.getCurrentUser().getUid());
        customsubref = mDatabase.child("customsubcats").child(mAuth.getCurrentUser().getUid());

        //inflate drawer and Nav view
        drawer = findViewById(R.id.home_drawer_layout);
        navigationView = findViewById(R.id.home_nav_view);

        //Navigation Drawer
        toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        //Navigation View
        navigationView.setNavigationItemSelectedListener(this);


        //Navigation menue
        menuee = navigationView.getMenu().getItem(0).getSubMenu();
        favmenu = navigationView.getMenu().getItem(1).getSubMenu();
        //logout item
        signoutmenueItem = navigationView.getMenu().findItem(R.id.logoutitem);


        //Get data from database
        db = DatabaseAcess.getInstance(this);
        db.open();
        main_categories = db.get_all_Main_categories();
        all_subcats = db.get_all_Sub_categories();
        db.close();


        //menue items
        for(int i = 0 ; i<main_categories.size();i++) {
            menuee.add(R.id.nav_drawer_group_menue,i,i,main_categories.get(i).getMain_category_name());
            menuee.getItem(i).setIcon(getResources().getIdentifier(main_categories.get(i).getMain_category_icon(),"drawable",getPackageName()));
        }


        //inflate navigation header view and pass data to it
        header_view = View.inflate(this,R.layout.nav_header_main,navigationView);
        userImage = header_view.findViewById(R.id.userImage);
        username = header_view.findViewById(R.id.username);
        profile = findViewById(R.id.profile);



        //search View
        search = findViewById(R.id.search);
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
                    bottomnavigationview.getMenu().getItem(0).setChecked(true);
                    getSupportFragmentManager().beginTransaction().replace(R.id.maincategoryfragment,  HomeMainCatFragment.newInstance(main_categories)).commit();
                }
                else{
                    for(Subcategory onesubcat : all_subcats){
                        if(onesubcat.getSub_category_name().toLowerCase().contains(newText)){
                            for (Subcategory fav : userFavoritesSubcats) {
                                if (onesubcat.getId() == fav.getId()) {
                                    onesubcat.setStar_state(1);
                                }
                            }
                            search_list.add(onesubcat);
                        }
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.maincategoryfragment, Sub_Category_Fragment.newInstance(search_list)).commit();
                }

                return false;
            }
        });


        //profile click listener
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(HomeMainCategories.this,ProfileActivity.class);
                startActivity(in);
            }
        });


        //sign out function
        signoutmenueItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mAuth.signOut();
                GoogleSignIn.getClient(HomeMainCategories.this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()).signOut()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                 SharedPreferenceManager preferenceManager = new SharedPreferenceManager(HomeMainCategories.this,"userdata");
                                 boolean cleared = preferenceManager.clear();
                                Log.d("looooooooooo", "onSuccess: sign out : "+cleared);

                                Intent in = new Intent(HomeMainCategories.this, Login.class);
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
                return false;
            }
        });


        //Bootom Navigation View-----------------------------------------------------------------------
        bottomnavigationview = findViewById(R.id.mainbottomview);

        //Fragments : -------------------------------------------------------------------------------
        getSupportFragmentManager().beginTransaction().replace(R.id.maincategoryfragment, HomeMainCatFragment.newInstance(main_categories)).commit();

        bottomnavigationview.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Home :
                        item.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.maincategoryfragment, HomeMainCatFragment.newInstance(main_categories)).commit();
                        break;
                    case R.id.Favorites :
                        item.setChecked(true);
                        if (new NetworkCheck(HomeMainCategories.this).isNetworkAvailable()){
                            if(userFavoritesSubcats.size()!=0){
                                getSupportFragmentManager().beginTransaction().replace(R.id.maincategoryfragment, Sub_Category_Fragment.newInstance(userFavoritesSubcats)).commit();
                            }
                            else{
                                getSupportFragmentManager().beginTransaction().replace(R.id.maincategoryfragment, ErrorFragment.newInstance(1)).commit();
                            }
                        }
                        else{
                            getSupportFragmentManager().beginTransaction().replace(R.id.maincategoryfragment, ErrorFragment.newInstance(0)).commit();

                        }
                        break;
                }

                return true;
            }
        });


        }
//--------------------------------------------------------------------------------------------------------


    private void gotoSubCategoryActivity(int mainCategoryindex , String title){
    db.open();
    categories = db.get_all_categories_by_maincatid(mainCategoryindex);  //get all categories by maincat
    Intent in = new Intent(getApplicationContext(), Sub_Categories_TabView_Activity.class);

    //compare favorites with subcategories to determine favorites subcats
    for (int i =0; i<categories.size();i++ ){
        subcats = db.get_subcategories_by_catid(categories.get(i).getCategory_id());

        //check for custom subcats
        if(usercustomsubcats_from_firebase!=null){
            for(Subcategory custom : usercustomsubcats_from_firebase) {
                if (categories.get(i).getCategory_id() == custom.getCategory_id()) {
                    subcats.add(custom);
                }
            }
        }

        //compare subcats with favorites list
        if (userFavoritesSubcats != null) {
            for (Subcategory subcat : subcats) {
                for (Subcategory fav : userFavoritesSubcats) {
                    if (subcat.getId() == fav.getId()) {
                        subcat.setStar_state(1);
                    }
                }
            }
        }
        in.putParcelableArrayListExtra("subcat"+i,subcats);
    }
    db.close();
    in.putParcelableArrayListExtra("allcats",categories);
    in.putExtra("title",title);
    startActivity(in);
}


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        gotoSubCategoryActivity(item.getItemId()+1 , item.getTitle().toString());
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        //get user info ...
        SharedPreferenceManager preferenceManager= new SharedPreferenceManager(this,"userdata");
        User user =  preferenceManager.getValue("data");
        Log.d("looooooooooooo", "onStart: values in sharedpref : "+user.getName()+" , "+user.getEmail()+" , "+user.getUid());
        //set user data
        username.setText(user.getName());
        Picasso.get().load(user.getImageurl().trim()).into(userImage);
        String [] firstname = user.getName().split(" ");
        mTitle.setText("Hello "+firstname[0] );


        //get user favorite list
        favoriteref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userFavoritesSubcats.clear();
                favmenu.clear();
                createandSettingBadger();
                badger.clearNumber();
                if(snapshot.hasChildren()){
                    badger.setNumber((int) snapshot.getChildrenCount());
                }
                else{
                    badger.setVisible(false);
                }

                for(DataSnapshot ds : snapshot.getChildren())
                {
                    if(ds != null){
                        String key = ds.getKey();
                        Subcategory fav = snapshot.child(key).getValue(Subcategory.class);
                        userFavoritesSubcats.add(fav);
                        favmenu.add(fav.getSub_category_name());
                        favmenu.getItem((favmenu.size())-1).setIcon(R.drawable.ic_star);

                    }
                }
                for(Subcategory favsubs : userFavoritesSubcats){
                    favsubs.setStar_state(1);
                }
                if(bottomnavigationview.getMenu().getItem(1).isChecked()){
                    getSupportFragmentManager().beginTransaction().replace(R.id.maincategoryfragment, Sub_Category_Fragment.newInstance(userFavoritesSubcats)).commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                badger.setVisible(false);
            }

        });


        //get user custom subcats list
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }

    @Override
    public void onBackPressed() {
        if (search.isActivated()) {
            search.clearFocus();
        }
    }

    @Override
    public void OnMainCategoryClick(int maincatid) {
        gotoSubCategoryActivity(maincatid,main_categories.get(maincatid-1).getMain_category_name());
    }

    public void createandSettingBadger(){
        badger = bottomnavigationview.getOrCreateBadge(R.id.Favorites);
        badger.setBackgroundColor(Color.RED);
        badger.setBadgeTextColor(Color.WHITE);
        badger.setVisible(true);
    }

    @Override
    public void OnFragmentInteraction(String subitemname, String uriImage) {
        ClickonSubcat obj = new ClickonSubcat();
        obj.GoToViewModelAfterClickOnSubcat(getApplicationContext(),subitemname,uriImage);
    }

    @Override
    public void onitemFragmentCheckStar(int id, String sub_category_name, String sub_category_image,int category_id, int star_state, int privacy) {

    }

    @Override
    public void onitemFragmentUncheckStar(int id, String sub_category_name, String sub_category_image,int category_id, int star_state, int privacy) {

        Query favkeyref = favoriteref.orderByChild("sub_category_name").equalTo(sub_category_name);
        favkeyref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                snapshot.getRef().setValue(null);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                 onStart();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}

