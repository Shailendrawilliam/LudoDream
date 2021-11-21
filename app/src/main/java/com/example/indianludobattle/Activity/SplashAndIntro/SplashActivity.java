package com.example.indianludobattle.Activity.SplashAndIntro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.indianludobattle.Activity.Auth.MobileRegisterActivity;
import com.example.indianludobattle.Activity.MainActivity;
import com.example.indianludobattle.MyUtil.MyPreferences;
import com.example.indianludobattle.R;

public class SplashActivity extends AppCompatActivity {

    Animation atg,atg2,atg3;
    ImageView ivLogo,ivLogo1,ivLogo2;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    MyPreferences myPreferences;
    String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        atg= AnimationUtils.loadAnimation(this,R.anim.slow_anim_two);
        atg2= AnimationUtils.loadAnimation(this,R.anim.slow_anim);
        atg3= AnimationUtils.loadAnimation(this,R.anim.item_animation_from_scale);
        ivLogo=findViewById(R.id.ivLogo);
        ivLogo1=findViewById(R.id.ivLogo1);
        ivLogo2=findViewById(R.id.ivLogo2);
        ivLogo.setAnimation(atg);
        ivLogo1.setAnimation(atg2);
        ivLogo2.setAnimation(atg3);
        myPreferences=new MyPreferences(this);
        UserId=myPreferences.getUserId();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(UserId!=null)
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
                else {
                    startActivity(new Intent(getApplicationContext(), MobileRegisterActivity.class));
                    finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}