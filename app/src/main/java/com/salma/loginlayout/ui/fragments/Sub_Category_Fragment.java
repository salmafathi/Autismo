package com.salma.loginlayout.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.salma.loginlayout.Adapters.Sub_Categories_RV_Adapter;
import com.salma.loginlayout.BuildConfig;
import com.salma.loginlayout.Interfaces.OnFavoritesClickListener;
import com.salma.loginlayout.Interfaces.OnFragmentClickListener;
import com.salma.loginlayout.Interfaces.OnFragmentFavoritesClickListener;
import com.salma.loginlayout.Interfaces.OnSubCardClickListener;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.Subcategory;

import java.util.ArrayList;

public class Sub_Category_Fragment extends Fragment {

    RecyclerView rv ;
    private ArrayList<Subcategory> sub_categories = new ArrayList<Subcategory>();
    public static final String key = "allsubcats";
    public static  Bundle args;
    OnFragmentClickListener listener ;
    OnFragmentFavoritesClickListener favlistener ;

    public Sub_Category_Fragment() {
    }

    public static Sub_Category_Fragment newInstance(ArrayList<Subcategory> all_sub_categories) {
        Sub_Category_Fragment fragment = new Sub_Category_Fragment();
        args = new Bundle();
        args.putParcelableArrayList(key,all_sub_categories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!= null) {
            sub_categories =getArguments().getParcelableArrayList(key);
        }
        else{
            Log.d("salmaaaaaaa", "onCreate: Iam in else in home fragment");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rv_sub_categories, container, false);
        //inflate recycleview
        rv= v.findViewById(R.id.rv);

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentClickListener || context instanceof OnFragmentFavoritesClickListener ) {
            listener = (OnFragmentClickListener) context;
            favlistener = (OnFragmentFavoritesClickListener) context;

        }

        else
            throw new  ClassCastException("you must implements your activity to on fragmentListener");
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        favlistener = null ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(),2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(lm);
        Sub_Categories_RV_Adapter ad = new Sub_Categories_RV_Adapter(sub_categories, new OnSubCardClickListener() {
            @Override
            public void onitemClick(String subitemname, String uriPhoto) {

                listener.OnFragmentInteraction(subitemname, uriPhoto);
            }
        }, new OnFavoritesClickListener() {


            @Override
            public void onCheckStar(int id, String sub_category_name, String sub_category_image,int category_id, int star_state, int privacy) {
            favlistener.onitemFragmentCheckStar( id, sub_category_name, sub_category_image,category_id, star_state,privacy);

            }

            @Override
            public void onUncheckStar(int id, String sub_category_name, String sub_category_image,int category_id, int star_state, int privacy) {
             favlistener.onitemFragmentUncheckStar( id,  sub_category_name,  sub_category_image,category_id, star_state,privacy);

            }
        });
        rv.setAdapter(ad);

    }
}