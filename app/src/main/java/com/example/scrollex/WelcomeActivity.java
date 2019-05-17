package com.example.scrollex;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    private  TextView txt1,txt2,txt3;
    Animation fadein,blinkAnim,RotateCycle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        txt1=findViewById(R.id.txtani1);
        txt2=findViewById(R.id.txtani2);
        txt3=findViewById(R.id.txtani3);
        fadein= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blade);
        blinkAnim=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in);
        RotateCycle=AnimationUtils.loadAnimation(getApplicationContext(),  R.anim.rotate);

        txt1.startAnimation(blinkAnim);
        txt2.startAnimation(fadein);
        txt3.startAnimation(RotateCycle);


        new CountDownTimer(7000, 1000){
            public void onTick(long millisUntilFinished){
            }
            public  void onFinish(){
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        }.start();
    }
}
