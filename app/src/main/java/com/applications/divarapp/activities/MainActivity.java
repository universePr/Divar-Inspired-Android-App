package com.applications.divarapp.activities;

import static android.Manifest.permission.CALL_PHONE;
import static android.os.Build.VERSION.SDK_INT;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.applications.divarapp.R;
import com.applications.divarapp.fragments.CategoriesFragment;
import com.applications.divarapp.fragments.ChatFragment;
import com.applications.divarapp.fragments.HomeFragment;
import com.applications.divarapp.fragments.LockChatFragment;
import com.applications.divarapp.fragments.SettingFragment;
import com.applications.divarapp.helpers.DBHelper;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 200;
    //Getting permissions
    private ArrayList<String> permissionsToRequest;
    private final ArrayList<String> permissions = new ArrayList<String>();
    private final static int ALL_PERMISSIONS_RESULT = 107;

    //Layout elements
    private BottomNavigationView navigationView;
    //Fragment transaction
    private FragmentTransaction fragmentTransaction;
    //
    // Create an Persian-English translator:
    public static TranslatorOptions optionsPersianToEnglish =
            new TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.PERSIAN)
                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                    .build();
    public static TranslatorOptions optionsSpanishToEnglish =
            new TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.SPANISH)
                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                    .build();

    public static TranslatorOptions optionsEnglishToSpanish =
            new TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.SPANISH)
                    .build();
    public static TranslatorOptions optionsEnglishToPersian =
            new TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(TranslateLanguage.PERSIAN)
                    .build();
    public static final Translator persianEnglishTranslator =
            Translation.getClient(optionsPersianToEnglish);

    public static final Translator spanishEnglishTranslator =
            Translation.getClient(optionsSpanishToEnglish);

    public static final Translator englishPersianTranslator =
            Translation.getClient(optionsEnglishToPersian);
    public static final Translator englishSpanishTranslator =
            Translation.getClient(optionsEnglishToSpanish);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DownloadModel();

        DBHelper db = new DBHelper(getApplicationContext());
        AssigningElements();

        navigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    if(!ThisIsCurrentFragment(Constants.Home_Fragment_Tag)) {
                        TransactionFragment(HomeFragment.newInstance(), Constants.Home_Fragment_Tag);
                        return true;
                    }
                    break;
                case R.id.chat:
                    if(!ThisIsCurrentFragment(Constants.Lock_FragmentChat_Tag)) {
                        if(UserLoggedIn()) {
                            TransactionFragment(ChatFragment.newInstance(), Constants.Chat_Fragment_Tag);
                        }else{
                            TransactionFragment(LockChatFragment.newInstance(), Constants.Lock_FragmentChat_Tag);
                        }
                        return true;
                    }
                    break;
                case R.id.categories:
                    if(!ThisIsCurrentFragment(Constants.Category_Fragment_Tag)) {
                        TransactionFragment(CategoriesFragment.newInstance(), Constants.Category_Fragment_Tag);
                        return true;
                    }
                    break;
                case R.id.ad:
                    if(UserLoggedIn()){
                        Intent i = new Intent(MainActivity.this,AdActivity.class);
                        startActivity(i);
                    }else{
                        Intent i = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(i);
                    }
                    break;
                case R.id.profile:
                    if(!ThisIsCurrentFragment(Constants.Setting_Fragment_Tag)) {
                        TransactionFragment(SettingFragment.newInstance(), Constants.Setting_Fragment_Tag);
                        return true;
                    }
                    break;
            }
            return false;
        });
        //permission checking
        permissions.add(CALL_PHONE);
        permissionsToRequest = findUnAskedPermissions(permissions);
        if (SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionsToRequest.size() > 0)
                requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        TransactionFragment(HomeFragment.newInstance(),Constants.Home_Fragment_Tag);
    }

    private void DownloadModel() {
        DownloadConditions conditions = new DownloadConditions.Builder()
                //.requireWifi()
                .build();

        persianEnglishTranslator.downloadModelIfNeeded(conditions);
        spanishEnglishTranslator.downloadModelIfNeeded(conditions);
        englishPersianTranslator.downloadModelIfNeeded(conditions);
        englishSpanishTranslator.downloadModelIfNeeded(conditions);

    }

    public BottomNavigationView getNavigationView() {
        return navigationView;
    }
    public void enableBottomBar(boolean enable){
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setEnabled(enable);
        }
    }

    //Finding layout elements
    private void AssigningElements() {
        navigationView = findViewById(R.id.navigation);
    }
    //Checking current fragment
    private boolean ThisIsCurrentFragment(String Tag){
        Fragment fragment =  getSupportFragmentManager().findFragmentByTag(Tag);
        if(fragment == null) return false;
        return fragment.isVisible();
    }
    //For transaction current fragment to input
    private void TransactionFragment(Fragment fragment, String Tag){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.page_frame, fragment,Tag);
        fragmentTransaction.addToBackStack(Tag);
        fragmentTransaction.commit();
    }
    //This calling by fragment
    public void ChangingFragment(Fragment fragment, String Tag){
        TransactionFragment(fragment, Tag);
    }
    //Checking user logged in
    private boolean UserLoggedIn() {
        Map<String, Object> map_sp = SPreferences.hasKey(Constants.Key_LoggedInUser_SP, this);
        return (boolean) map_sp.get("1");
    }


    // Permissions control
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }
    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }
    private boolean canMakeSmores() {
        return (SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean CALL_PHONE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    //boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (/*READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE*/CALL_PHONE) {
                        // perform action when allow permission success
                    } else {
                        Toast.makeText(this, R.string.allow_permission, Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
    //On Back press button
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        UserLoggedIn();
        super.onResume();
    }
}