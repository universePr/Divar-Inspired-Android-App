package com.applications.divarapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.applications.divarapp.utils.LanguageManager;
import com.applications.divarapp.utils.SPreferences;

import java.util.Map;

public class AppCompat extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Map<String,Object> map = SPreferences.hasKey("lang", this);
        if ((boolean) map.get("1")) {
            LanguageManager languageManager = new LanguageManager(this);
            languageManager.updateResource(languageManager.getLang());
        }else{
            SPreferences.setDefaults("lang","en",this);
        }
    }
}
