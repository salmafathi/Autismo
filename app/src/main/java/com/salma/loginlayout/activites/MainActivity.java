package com.salma.loginlayout.activites;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ProgressBar;

import com.salma.loginlayout.R;

public class MainActivity extends AppCompatActivity {
    int progress = 20 ;
    private Handler handler = new Handler();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final ProgressBar pbar = (ProgressBar) findViewById(R.id.progressBar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress<100){
                    progress += 10 ;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            pbar.getProgressDrawable().setColorFilter(Color.argb(100,3,169,244), android.graphics.PorterDuff.Mode.SRC_IN);
                            pbar.setProgress(progress);
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                final Intent mainIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(mainIntent);
                finish();
            }
        }).start();
      /*  pbar.setProgress(progress);
        if(progress==pbar.getMax()){
            final Intent mainIntent = new Intent(getApplicationContext(), Login.class);
            startActivity(mainIntent);
            finish();
        }*/



       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(getApplicationContext(), Login.class);
                startActivity(mainIntent);
                finish();
            }
        }, 2000); */
    }


}