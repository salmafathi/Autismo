package com.salma.loginlayout.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.salma.loginlayout.DragTransformableNode;
import com.salma.loginlayout.R;

import java.util.ArrayList;



public class three_D_Fragment extends Fragment {
    SceneView sv ;
    ModelRenderable model ;
    ArrayList<String> modelinfo;
    public static final String key = "modelInformation";


  //  private OnFragmentInteractionListener mListener;

    public three_D_Fragment() {
        // Required empty public constructor
    }


    public static three_D_Fragment newInstance(ArrayList<String> modelinfo) {
        three_D_Fragment fragment = new three_D_Fragment();
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
        else{
            Log.d(key, "onCreate: Iam in else in home fragment");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_three__d_, container, false);
        sv = v.findViewById(R.id.scene_view);
        return v ;
    }

    private void createScene() {
        //   mScene = new Scene(sv);
        Log.d(key, "createScene: " +modelinfo);
        ModelRenderable
                .builder()
                .setSource(getContext(), Uri.parse(modelinfo.get(0)))
                .setRegistryId(modelinfo.get(0))
                .build()
                .thenAccept(renderable -> onRenderableLoaded(renderable))
                .exceptionally(throwable -> {
                    Toast toast = Toast.makeText(getContext(), "Unable to load andy renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

    }

    private void onRenderableLoaded(Renderable renderable) {

        if(sv!=null){

            TransformationSystem tr = transformationsystem();
            DragTransformableNode drnode = new DragTransformableNode (9f,tr);
            drnode.setLocalPosition(new Vector3(-1f,-1f,-2f));
            drnode.setLocalScale(new Vector3(2f,2f,2f));
            drnode.setRenderable(renderable);
            sv.getScene().addChild(drnode);
            drnode.select();
            sv.getScene().addOnPeekTouchListener((HitTestResult hitTestResult, MotionEvent motionEvent) ->{
                tr.onTouch(hitTestResult,motionEvent);

            });

        }
    }


    private TransformationSystem transformationsystem (){
        FootprintSelectionVisualizer footprint = new FootprintSelectionVisualizer() ;
        return new TransformationSystem(getResources().getDisplayMetrics() ,footprint);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        createScene();

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            sv.resume();
        } catch (CameraNotAvailableException e) {
            e.printStackTrace();
        }
    }
}
