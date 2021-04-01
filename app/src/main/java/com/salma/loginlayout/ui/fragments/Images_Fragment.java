package com.salma.loginlayout.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.salma.loginlayout.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class Images_Fragment extends Fragment {



    private ImageView ImageModel;
    ArrayList<String> modelinfo;
    public static final String key = "modelInformation";

    public Images_Fragment() {
        // Required empty public constructor
    }

    public static Images_Fragment newInstance(ArrayList<String> modelinfo) {
        Images_Fragment fragment = new Images_Fragment();
        Bundle args = new Bundle();
        args.putStringArrayList(key,modelinfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            modelinfo = getArguments().getStringArrayList(key);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_images_, container, false);
        ImageModel = v.findViewById(R.id.modelImage);
        return v;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(key, "onActivityCreated: "+modelinfo.get(2));
            Picasso.get().load(modelinfo.get(2)).into(ImageModel);

    }
}
