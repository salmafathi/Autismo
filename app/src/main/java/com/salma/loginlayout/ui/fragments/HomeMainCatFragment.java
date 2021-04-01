package com.salma.loginlayout.ui.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salma.loginlayout.Adapters.MainCatergoriesRV_Adapter;
import com.salma.loginlayout.Interfaces.OnMainCardClickListener;
import com.salma.loginlayout.R;
import com.salma.loginlayout.database.Main_Categories;

import java.util.ArrayList;

public class HomeMainCatFragment extends Fragment {

    RecyclerView rv ;
    ArrayList<Main_Categories> main_categories = new ArrayList<>();
    public static final String key = "allMaincats";
    OnMainCategoryClickListener listener ;

    public HomeMainCatFragment() {
        // Required empty public constructor
    }

    public static HomeMainCatFragment newInstance(ArrayList<Main_Categories>main_categories) {
        HomeMainCatFragment fragment = new HomeMainCatFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(key,main_categories);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            main_categories =getArguments().getParcelableArrayList(key);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home_main_cat, container, false);
        rv= v.findViewById(R.id.maincatrv);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecyclerView.LayoutManager lm = new GridLayoutManager(getActivity(),2);
        rv.setHasFixedSize(false);
        rv.setLayoutManager(lm);
        MainCatergoriesRV_Adapter ad = new MainCatergoriesRV_Adapter(main_categories, new OnMainCardClickListener() {
            @Override
            public void onMainCarditemClick(int MainCategoryid) {
               listener.OnMainCategoryClick(MainCategoryid);
            }
        },getContext());
        rv.setAdapter(ad);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof HomeMainCatFragment.OnMainCategoryClickListener){
            listener = (OnMainCategoryClickListener) context;
        }
        else
            throw new  ClassCastException("you must implements your activity to on fragmentListener");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface OnMainCategoryClickListener {
        void OnMainCategoryClick(int maincatid);
    }
}