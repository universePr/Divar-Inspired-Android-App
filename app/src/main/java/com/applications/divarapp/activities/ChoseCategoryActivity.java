package com.applications.divarapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.applications.divarapp.R;
import com.applications.divarapp.fragments.SubCategoryFragment;
import com.applications.divarapp.models.CategoryModel;
import com.applications.divarapp.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ChoseCategoryActivity extends AppCompatActivity {

    //Fragment transaction
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosse_category);
        Intent i = getIntent();
        ArrayList<CategoryModel.SubCategoryModel> subs = i.getParcelableArrayListExtra("subs");
        TransactionFragment(
                SubCategoryFragment.newInstance(
                        i.getStringExtra("cname"),i.getStringExtra("cicon"),subs,Constants.page_state.SEARCH,i.getIntExtra("cid",0),-1
                ),Constants.SubCategory_Search_Fragment_Tag);
    }
    //For transaction current fragment to input
    private void TransactionFragment(Fragment fragment, String Tag){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.page_frame, fragment,Tag);
        fragmentTransaction.addToBackStack(Tag);
        fragmentTransaction.commit();
    }
    //Change fragment
    public void ChangingFragment(Fragment fragment, String Tag){
        TransactionFragment(fragment, Tag);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}