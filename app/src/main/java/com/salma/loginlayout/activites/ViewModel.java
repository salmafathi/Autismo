package com.salma.loginlayout.activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.SceneView;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.collision.CollisionShape;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.RenderableInstance;
import com.google.ar.sceneform.ux.DragGestureRecognizer;
import com.google.ar.sceneform.ux.FootprintSelectionVisualizer;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;
import com.salma.loginlayout.DragTransformableNode;
import com.salma.loginlayout.R;
import com.salma.loginlayout.activites.Login;
import com.salma.loginlayout.ui.fragments.Images_Fragment;
import com.salma.loginlayout.ui.fragments.three_D_Fragment;

import java.io.IOException;
import java.util.ArrayList;

public class ViewModel extends AppCompatActivity  {


    ArrayList<String> modelinfo;
    MediaPlayer ring;
    BottomNavigationView bootom_nav_view ;
    CountDownTimer timer ;
    final static String key = "sooooound" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_model);

        Bundle extras =getIntent().getExtras();
        modelinfo = (ArrayList<String>) extras.get("SubCategoryName");

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMediaView, three_D_Fragment.newInstance(modelinfo)).commit();

        bootom_nav_view = (BottomNavigationView)findViewById(R.id.buttom_nav_bar);
        bootom_nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.p3D :
                        item.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMediaView, three_D_Fragment.newInstance(modelinfo)).commit();
                        break;

                    case R.id.p2D :
                        item.setChecked(true);
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentMediaView, Images_Fragment.newInstance(modelinfo)).commit();
                        break;

                    case R.id.pause :
                        item.setChecked(true);
                        if(ring.isPlaying()){
                            item.setIcon(R.drawable.ic_play_button);
                            item.setTitle("play");
                            ring.pause();
                        }
                        else {
                            item.setIcon(R.drawable.ic_pause_symbol);
                            item.setTitle("pause");
                            ring.start();
                            ring.setLooping(true);
                        }
                        break ;
                }

                return true;
            }
        });



        int id = getResources().getIdentifier(modelinfo.get(1), "raw", getPackageName());
        ring= MediaPlayer.create(this, id);
        ring.start();
        ring.setLooping(true);
    }




    @Override
    protected void onPause() {
        super.onPause();
        ring.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ring.stop();
    }



}



