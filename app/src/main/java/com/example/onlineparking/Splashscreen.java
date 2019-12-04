package com.example.onlineparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Splashscreen extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;
    private FirebaseAuth firebaseAuth;
    private static int SPLASH_TIME_OUT = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        tv = findViewById(R.id.tv);
        firebaseAuth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent startLoginPage = new Intent(Splashscreen.this, Login_Form.class);
                    startActivity(startLoginPage);
                } else {
                    Intent startDashboard = new Intent(Splashscreen.this, Dashboard.class);
                    startActivity(startDashboard);
                }


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}
