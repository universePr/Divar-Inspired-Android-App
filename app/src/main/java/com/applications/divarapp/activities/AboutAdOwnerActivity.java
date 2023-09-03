package com.applications.divarapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.divarapp.R;
import com.applications.divarapp.adapters.MyAdListAdapter;
import com.applications.divarapp.databinding.ActivityAboutOwnerAdBinding;
import com.applications.divarapp.models.AdsModel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutAdOwnerActivity extends AppCompatActivity {
    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);
    //Ad adapter
    private MyAdListAdapter adapterAd;
    //Binding
    private ActivityAboutOwnerAdBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAboutOwnerAdBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        SetListeners();
        GetOwnerAdsFilter(getIntent().getStringExtra("phone"), Constants.FilterByUser,"temp");
    }

    private void SetListeners() {
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void GetOwnerAdsFilter(String input , int filter, String input2) {
        endpoints.FilterAds(input, filter,input2).enqueue(new Callback<ArrayList<AdsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<AdsModel>> call, Response<ArrayList<AdsModel>> response) {
                if(response.body() != null){
                    binding.txtMember.setText(response.body().get(0).getMembership());
                    binding.txtAds.setText(response.body().size() + "");
                    binding.adsList.setLayoutManager(new LinearLayoutManager(AboutAdOwnerActivity.this));
                    adapterAd = new MyAdListAdapter(response.body(),AboutAdOwnerActivity.this, item -> {
                        if(!item.isHide()) {
                            Intent i = new Intent(AboutAdOwnerActivity.this, AdShowActivity.class);
                            i.putExtra(Constants.Key_Extra_AId, item);
                            startActivity(i);
                        }
                    });
                    binding.adsList.setAdapter(adapterAd);
                    binding.progressCircular.setVisibility(View.GONE);
                    binding.dataContent.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AdsModel>> call, Throwable t) {
                Snackbar.make(
                        binding.content, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.try_again), v -> GetOwnerAdsFilter(input, filter, input2)).show();

            }
        });


    }
}