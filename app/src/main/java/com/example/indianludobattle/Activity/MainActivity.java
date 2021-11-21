package com.example.indianludobattle.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.indianludobattle.Activity.Contest.ContestActivity;
import com.example.indianludobattle.Activity.CountDown.CountActivity;
import com.example.indianludobattle.Activity.Ludo.Ludo;
import com.example.indianludobattle.Activity.Ludo.LudoActivity;
import com.example.indianludobattle.Activity.Ludo.NewLudoActivity;
import com.example.indianludobattle.Activity.Profile.ProfileActivity;
import com.example.indianludobattle.MyUtil.MyPreferences;
import com.example.indianludobattle.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.ivPlay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ContestActivity.class));
            }
        });
        findViewById(R.id.ivSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        findViewById(R.id.tvPlayer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPreferences myPreferences = new MyPreferences(getApplicationContext());
                myPreferences.setName("Suraj");
                myPreferences.setUserId("1234567890");
                startActivity(new Intent(getApplicationContext(), NewLudoActivity.class));
            }
        });

        findViewById(R.id.tvPlayer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPreferences myPreferences = new MyPreferences(getApplicationContext());
                myPreferences.setName("Radha");
                myPreferences.setUserId("9876543210");
                startActivity(new Intent(getApplicationContext(), NewLudoActivity.class));
            }
        });
    }
}