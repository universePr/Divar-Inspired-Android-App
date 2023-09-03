package com.applications.divarapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.applications.divarapp.R;
import com.applications.divarapp.activities.AdShowActivity;
import com.applications.divarapp.activities.ChoseCategoryActivity;
import com.applications.divarapp.activities.CityActivity;
import com.applications.divarapp.adapters.AdListAdapter;
import com.applications.divarapp.adapters.CategoryListAdapter;
import com.applications.divarapp.databinding.FragmentHomeBinding;
import com.applications.divarapp.models.AdsModel;
import com.applications.divarapp.models.CategoryModel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);
    //Ad adapter
    private AdListAdapter adapterAd;
    private CategoryListAdapter adapterCat;
    //User city id
    private int cityId;

    //Binding
    private FragmentHomeBinding binding;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AssigningElements(view);
        SetListeners(view);
        //GetNewAds();
        GetNewAdsFilter(cityId+"", Constants.FilterByCity,"temp");
        GetCategories();
    }
    //Get categories
    private void GetCategories() {
        endpoints.GetCategories(Constants.Secret_Categories).enqueue(new Callback<ArrayList<CategoryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryModel>> call, Response<ArrayList<CategoryModel>> response) {
                if(response.body() != null) {
                    binding.categoriesList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true));
                    adapterCat = new CategoryListAdapter(response.body(), getContext(), item -> {
                        Intent i = new Intent(getActivity(), ChoseCategoryActivity.class);
                        i.putExtra("cid", item.getId());
                        i.putExtra("cname", item.getName());
                        i.putExtra("cicon", item.getIconUrl());
                        i.putExtra("subs", item.getSubs());
                        getContext().startActivity(i);
                    });
                    binding.categoriesList.setAdapter(adapterCat);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryModel>> call, Throwable t) {
                try {
                    Snackbar.make(binding.content, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getString(R.string.try_again), v -> GetCategories()).show();
                }catch (Exception e){
                    Toast.makeText(getContext(), getString(R.string.communication_problem_has_occurred), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    //Getting ad
    private void GetNewAds() {
        endpoints.GetNewAds().enqueue(new Callback<ArrayList<AdsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<AdsModel>> call, Response<ArrayList<AdsModel>> response) {
                if(response.body() != null){

                    binding.adsList.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterAd = new AdListAdapter(response.body(), getContext(), item -> {
                        Intent i = new Intent(getActivity(), AdShowActivity.class);
                        i.putExtra(Constants.Key_Extra_AId, item);
                        startActivity(i);
                    });
                    binding.adsList.setAdapter(adapterAd);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<AdsModel>> call, Throwable t) {
                try {
                    Snackbar.make(binding.content, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.try_again), v -> GetNewAds()).show();
                }catch (Exception e){
                    Toast.makeText(getContext(), getString(R.string.communication_problem_has_occurred), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //Getting ad in user location
    private void GetNewAdsFilter(String input , int filter, String input2) {
        endpoints.FilterAds(input, filter,input2).enqueue(new Callback<ArrayList<AdsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<AdsModel>> call, Response<ArrayList<AdsModel>> response) {
                if(response.body() != null){

                    binding.adsList.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterAd = new AdListAdapter(response.body(),getContext(),item -> {
                        Intent i = new Intent(getActivity(), AdShowActivity.class);
                        i.putExtra(Constants.Key_Extra_AId, item);
                        startActivity(i);
                    });
                    binding.adsList.setAdapter(adapterAd);
                }else{
                    try {
                        Snackbar.make(binding.content, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.try_again), v -> GetNewAds()).show();
                    }catch (Exception e){
                        Toast.makeText(getContext(), getString(R.string.communication_problem_has_occurred), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AdsModel>> call, Throwable t) {
                try {
                    Snackbar.make(binding.content, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.try_again), v -> GetNewAds()).show();
                }catch (Exception e){
                    Toast.makeText(getContext(), getString(R.string.communication_problem_has_occurred), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void AssigningElements(View view) {
        binding.txtCity.setText(SPreferences.getDefaults(Constants.Key_City_SP, getContext()).split(":")[1]);
        cityId = Integer.parseInt(SPreferences.getDefaults(Constants.Key_City_SP, getContext()).split(":")[0]);
        binding.btnLocation.setOnClickListener(v -> {
            ChangeLocation();
        });
        binding.txtCity.setOnClickListener(v -> {
            ChangeLocation();
        });
    }
    private void ChangeLocation(){
        Intent i = new Intent(getActivity(), CityActivity.class);
        i.putExtra(Constants.Source_Key, Constants.Source_Home_City_Selection);
        startActivity(i);
    }
    private void SetListeners(View view) {
        binding.txtSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() != 0) {
                    GetNewAdsFilter(s.toString(), Constants.FilterByName,"temp");
                }else{
                    GetNewAdsFilter(cityId+"", Constants.FilterByCity,"temp");
                }

            }
        });

    }
}