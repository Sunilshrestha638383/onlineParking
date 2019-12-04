package com.example.onlineparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class Splashscreen extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        tv=(TextView)findViewById(R.id.tv);
        iv=(ImageView)findViewById(R.id.iv);
        firebaseAuth=FirebaseAuth.getInstance();
       final Intent i=new Intent(this,Login_Form.class);
        Animation myanim= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tv.startAnimation(myanim);
        iv.startAnimation(myanim);
        Thread timer=new Thread(){
            public void run(){
                try{
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    if(firebaseAuth.getCurrentUser()==null){
                        Intent startLoginPage= new Intent(Splashscreen.this,Login_Form.class);
                        startActivity(startLoginPage);
                    }
                    else{
                        Intent startDashboard=new Intent(Splashscreen.this,Dashboard.class);
                        startActivity(startDashboard);
                    }


                }
            }

        };
        timer.start();

    }
}
