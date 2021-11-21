package com.example.indianludobattle.Activity.Ludo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.indianludobattle.R;

public class DemoActivity extends AppCompatActivity {

    TextView tvBottom;
    ImageView move;
    int tick= 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        tvBottom = findViewById(R.id.tvBottom);
        move = findViewById(R.id.move);
        tvBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tick==0){
                    move.setVisibility(View.VISIBLE);
                    move.animate().x(230f).y(150f).setDuration(1000).setInterpolator( new BounceInterpolator());;
                    tick++;
                } else if(tick==1){
                    move.setVisibility(View.VISIBLE);
                    move.animate().x(230f).y(350f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                } else if(tick==2){
                    move.setVisibility(View.VISIBLE);
                    move.animate().x(230f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }
                else if(tick==3){
                    move.setVisibility(View.VISIBLE);
                    // move.startAnimation();
                    move.animate().x(230f).y(850f).setDuration(500).setDuration(1000).setInterpolator( new BounceInterpolator() );
                    tick++;
                } else if(tick==4){
                    move.setVisibility(View.VISIBLE);
                    move.animate().x(230f).y(1080f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==5){
                    move.setVisibility(View.VISIBLE);
                    move.animate().x(450f).y(1080f);
                    tick++;
                }else if(tick==6){
                    move.setVisibility(View.VISIBLE);
                    move.animate().x(720f).y(1080f);
                    tick++;
                }else if(tick==7){
                    move.setVisibility(View.VISIBLE);
                    move.animate().x(720f).y(850f);
                    tick++;
                }else if(tick==8){
                    move.setVisibility(View.VISIBLE);
                    move.animate().x(720f).y(550f);
                    tick++;
                }else if(tick==9){
                    move.setVisibility(View.VISIBLE);
                    move.animate().x(720f).y(350f);
                    tick++;
                }else if(tick==10){
                    move.setVisibility(View.VISIBLE);
                    move.animate().x(720f).y(150f);
                    tick++;
                }

            }
        });
    }
}