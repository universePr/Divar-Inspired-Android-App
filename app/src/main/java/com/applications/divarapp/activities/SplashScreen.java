package com.applications.divarapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.applications.divarapp.R;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;

import java.util.Map;

public class SplashScreen extends AppCompat {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        Map<String, Object> map_sp = SPreferences.hasKey(Constants.Key_City_SP, this);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if((boolean) map_sp.get("1")){
                    Intent i = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(SplashScreen.this,FirstActivity.class);
                    startActivity(i);
                }

            }
        }, 800);

    }

}