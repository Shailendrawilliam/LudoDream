package com.example.indianludobattle.Activity.Ludo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.example.indianludobattle.R;

public class Ludo extends AppCompatActivity {
    int tick= 0;
    ImageView move;
    float xAxis[]={425f,425f,425f,425f,360f,290f,220f,150f,85f,20f,20f,20f,85f,150f,220f,290f,360f,425f,425f,425f,6};
    float yAxis[]={810f,740f,680f,610f,550f,550f,550f,550f,550f,550f,480f,410f,410f,410f,410f,410f,410f,340f,270f,200f,6};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ludo2);
        move = findViewById(R.id.pieseYellowOne);
        findViewById(R.id.tvPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tick==0){
                    move.animate().x(425f).y(810f).setDuration(500).setInterpolator( new BounceInterpolator()); //-40
                    tick++;
                } else if(tick==1){
                    move.animate().x(425f).y(740f).setDuration(500).setInterpolator( new BounceInterpolator()); //-40
                    tick++;
                } else if(tick==2){

                    move.animate().x(425f).y(680f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }
                else if(tick==3){

                    // move.startAnimation();
                    move.animate().x(425f).y(610f).setDuration(500).setInterpolator( new BounceInterpolator() );
                    tick++;
                }
                else if(tick==4){

                    move.animate().x(360f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==5){

                    move.animate().x(290f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==6){

                    move.animate().x(220f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==7){

                    move.animate().x(150f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }
                else if(tick==8){
                    move.animate().x(85f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==9){

                    move.animate().x(20f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==10){

                    move.animate().x(20f).y(480f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==11){

                    move.animate().x(20f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }
                else if(tick==12){
                    move.animate().x(85f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==13){
                    move.animate().x(150f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==14){

                    move.animate().x(220f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==15){

                    move.animate().x(290f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }
                else if(tick==16){

                    move.animate().x(360f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==17){

                    move.animate().x(425f).y(340f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==18){

                    move.animate().x(425f).y(270f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==19){
                    
                    move.animate().x(425f).y(200f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }
                else if(tick==20){
                    
                    move.animate().x(425f).y(130f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==21){
                    
                    move.animate().x(425f).y(70f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==22){
                    
                    move.animate().x(425f).y(0f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==23){
                    
                    move.animate().x(495f).y(0f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==24){
                    
                    move.animate().x(565f).y(0f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==25){
                    
                    move.animate().x(565f).y(70f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==26){
                    
                    move.animate().x(565f).y(140f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==27){
                    
                    move.animate().x(565f).y(210f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==28){
                    
                    move.animate().x(565f).y(280f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==29){
                    
                    move.animate().x(565f).y(350f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==30){
                    
                    move.animate().x(635f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==31){
                    
                    move.animate().x(700f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==32){
                    
                    move.animate().x(770f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==33){
                    
                    move.animate().x(840f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==34){
                    
                    move.animate().x(910f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==35){
                    
                    move.animate().x(980f).y(410f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==36){
                    
                    move.animate().x(980f).y(480f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==37){
                    
                    move.animate().x(980f).y(480f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }
                else if(tick==38){
                    
                    move.animate().x(980f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }
                else if(tick==39){
                    
                    move.animate().x(910f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==40){
                    
                    move.animate().x(840f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==41){
                    
                    move.animate().x(770f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==42){
                    
                    move.animate().x(700f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==43){
                    
                    move.animate().x(630f).y(550f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==44){
                    
                    move.animate().x(565f).y(620f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==45){
                    
                    move.animate().x(565f).y(680f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }
                else if(tick==46){
                    
                    move.animate().x(565f).y(750f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==47){
                    
                    move.animate().x(565f).y(820f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==48){
                    
                    move.animate().x(565f).y(890f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==49){
                    
                    move.animate().x(565f).y(960f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }else if(tick==50){
                    
                    move.animate().x(490f).y(960f).setDuration(500).setInterpolator( new BounceInterpolator());
                    tick++;
                }

            }
        });
    }
}