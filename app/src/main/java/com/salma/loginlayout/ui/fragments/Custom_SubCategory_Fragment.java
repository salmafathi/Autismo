package com.salma.loginlayout.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salma.loginlayout.Adapters.Custom_Sub_category_Adapter;
import com.salma.loginlayout.Interfaces.OnCustomAdapterAddClickListener;
import com.salma.loginlayout.Interfaces.OnCustomFragmentAddListener;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.Subcategory;

import java.util.ArrayList;

public class Custom_SubCategory_Fragment extends Fragment {

    RecyclerView rv ;
    private ArrayList<Subcategory> custom_sub_categories = new ArrayList<Subcategory>();
    public static final String key = "allcustomsubcats";
    public static  Bundle args;
    OnCustomFragmentAddListener addlistener;

    public Custom_SubCategory_Fragment() {
        // Required empty public constructor
    }

    public static Custom_SubCategory_Fragment newInstance(ArrayList<Subcategory> custom_sub_categories) {
        Custom_SubCategory_Fragment fragment = new Custom_SubCategory_Fragment();
        args = new Bundle();
        args.putParcelableArrayList(key,custom_sub_categories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments()!= null) {
            custom_sub_categories =getArguments().getParcelableArrayList(key);
        }
        else{
            Log.d("salmaaaaaaa", "onCreate: Iam in else in home fragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_custom_subcategory, container, false);
        rv= v.findViewById(R.id.custom_rv);
        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnCustomFragmentAddListener){
            addlistener = (OnCustomFragmentAddListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        addlistener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(),1);
       // rv.setHasFixedSize(true);
        rv.setLayoutManager(lm);
        Custom_Sub_category_Adapter ad = new Custom_Sub_category_Adapter(getContext() ,custom_sub_categories, new OnCustomAdapterAddClickListener() {

            @Override
            public void OnAddListner(int customSubCat_id, String customName, String CustomImage, int CustomCategory, int customStarState, int customPrivacy) {
                addlistener.CustomFragmentAddListener(customSubCat_id,customName,CustomImage,CustomCategory,customStarState,customPrivacy);
            }
        });
        rv.setAdapter(ad);
    }
}