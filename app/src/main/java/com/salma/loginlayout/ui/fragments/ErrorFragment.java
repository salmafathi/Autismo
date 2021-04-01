package com.salma.loginlayout.ui.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.salma.loginlayout.R;

import org.w3c.dom.Text;

public class ErrorFragment extends Fragment {

    private static final String KEY = "param1";
    ImageView errorimage ;
    TextView errortext;

    // TODO: Rename and change types of parameters
    private int errortype;

    public ErrorFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ErrorFragment newInstance(int errortype) {
        ErrorFragment fragment = new ErrorFragment();
        Bundle args = new Bundle();
        args.putInt(KEY, errortype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            errortype = getArguments().getInt(KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         View v  = inflater.inflate(R.layout.fragment_error, container, false);
        errorimage = v.findViewById(R.id.errorimage);
        errortext =  v.findViewById(R.id.errortext);
        return v ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        switch (errortype){
            case 0 :
                errortext.setText("No Internet Connection !");
                errorimage.setImageResource(R.drawable.ic_warning_sign_on_a_triangular_background);
                break;

            case 1 :
                errortext.setText("No favorites yet !");
                errorimage.setImageResource(R.drawable.ic_staroff);
                break;
        }

    }
}