package com.applications.divarapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.applications.divarapp.R;
import com.applications.divarapp.utils.Constants;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        ((Button)findViewById(R.id.textButton)).setOnClickListener(v -> {
            Intent i = new Intent(FirstActivity.this, CityActivity.class);
            i.putExtra(Constants.Source_Key,Constants.Source_First_City_Selection);
            startActivity(i);
        });
    }

    @Override
    public void onBackPressed() {

    }
}