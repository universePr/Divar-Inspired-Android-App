package com.applications.divarapp.activities;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.applications.divarapp.R;
import com.applications.divarapp.fragments.SelecteCategoriesFragment;
import com.applications.divarapp.utils.Constants;

public class AdActivity extends AppCompatActivity {

    //Fragment transaction
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        ChangingFragment(SelecteCategoriesFragment.newInstance(), Constants.ADCategory_Fragment_Tag);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    //Checking current fragment
    private boolean ThisIsCurrentFragment(String Tag){
        Fragment fragment =  getSupportFragmentManager().findFragmentByTag(Tag);
        if(fragment == null) return false;
        return fragment.isVisible();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}