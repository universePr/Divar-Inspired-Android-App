package com.applications.divarapp.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LanguageManager {
    private final Context context;

    public LanguageManager(Context context) {
        this.context = context;
    }
    public String getLang(){
        return SPreferences.getDefaults("lang", context);
    }
    public void updateResource(String code){
        Locale locale = new Locale(code);

        Locale.setDefault(locale);
        Resources resource = context.getResources();
        Configuration configuration = resource.getConfiguration();
        if (Build.VERSION.SDK_INT >= 17) {
            configuration.setLocale(locale);
        } else {
            configuration.locale = locale;
        }

        configuration.setLayoutDirection(locale);
        resource.updateConfiguration(configuration,resource.getDisplayMetrics());
        SPreferences.setDefaults("lang",code,context);
    }
}
