//Pre-launch app interface / loading screen
package com.example.bobaly;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Welcome extends AppCompatActivity {
    private static int LAUNCH_TIME_OUT = 3000; //3 second for welcome page
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        progressBar = findViewById(R.id.progressBar2);
        loadProgress();

        //welcome page settings
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Welcome.this, LogInActivity.class);//from welcome page to sign up
                startActivity(i); //start main activity
                finish();//finish welcome activity
            }
        }, LAUNCH_TIME_OUT);
        //end of welcome page settings
    }


    //for progress bar
    private void loadProgress() {
        ObjectAnimator.ofInt(progressBar, "progress", 100)
                .setDuration(2000) //run 2 sec each time (4 sec in total)
                .start();
    }
    //end of for progress bar
}