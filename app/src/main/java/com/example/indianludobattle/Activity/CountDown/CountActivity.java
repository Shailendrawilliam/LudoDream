package com.example.indianludobattle.Activity.CountDown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.indianludobattle.Activity.Auth.MobileRegisterActivity;
import com.example.indianludobattle.Activity.Ludo.LudoActivity;
import com.example.indianludobattle.Activity.Ludo.NewLudoActivity;
import com.example.indianludobattle.Activity.MainActivity;
import com.example.indianludobattle.R;

public class CountActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(() -> {
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                        startActivity(new Intent(getApplicationContext(), NewLudoActivity.class));
                        finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }, 1000);
    }
}