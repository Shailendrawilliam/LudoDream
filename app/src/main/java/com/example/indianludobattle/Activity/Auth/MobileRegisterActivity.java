package com.example.indianludobattle.Activity.Auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.indianludobattle.Activity.Ludo.NewLudoActivity;
import com.example.indianludobattle.MyUtil.MyPreferences;
import com.example.indianludobattle.R;

public class MobileRegisterActivity extends AppCompatActivity {

    EditText etMobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_register);
        etMobile=findViewById(R.id.etMobile);
        findViewById(R.id.tvContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyPreferences myPreferences = new MyPreferences(getApplicationContext());
                myPreferences.setName("Player");
                myPreferences.setUserId(etMobile.getText().toString());
                startActivity(new Intent(getApplicationContext(), MobileOtpActivity.class));
            }
        });
    }
}