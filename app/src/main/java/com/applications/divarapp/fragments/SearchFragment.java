package com.applications.divarapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.applications.divarapp.R;
import com.applications.divarapp.activities.AdShowActivity;
import com.applications.divarapp.adapters.AdListAdapter;
import com.applications.divarapp.databinding.FragmentSearchBinding;
import com.applications.divarapp.models.AdsModel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);
    //Arguments
    private static final String ARG_CATEGORY_ID = "category_id";
    private static final String ARG_SCATEGORY_ID = "scategory_id";
    private static final String ARG_SSCATEGORY_ID = "sscategory_id";
    private static final String ARG_TEXT = "input";

    //Ad adapter
    private AdListAdapter adapterAd;
    //User city id
    private int cityId;
    //Categories
    private  int categoryId;
    private  int sCategoryId;
    private  int ssCategoryId;
    //Binding
    private FragmentSearchBinding binding;


    public static SearchFragment newInstance(int cid, int sid, int ssid, String input) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CATEGORY_ID, cid);
        args.putInt(ARG_SCATEGORY_ID, sid);
        args.putInt(ARG_SSCATEGORY_ID, ssid);
        args.putString(ARG_TEXT, input);
        fragment.setArguments(args);
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
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cityId = Integer.parseInt(SPreferences.getDefaults(Constants.Key_City_SP, getContext()).split(":")[0]);

        SetListeners(view);
        categoryId = getArguments().getInt(ARG_CATEGORY_ID);
        sCategoryId = getArguments().getInt(ARG_SCATEGORY_ID);
        ssCategoryId = getArguments().getInt(ARG_SSCATEGORY_ID);
        if(ssCategoryId != -1){
            GetNewAdsFilter("*");
        }else if(sCategoryId != -1){
            GetNewAdsFilter("*");
        }else{
            GetNewAdsFilter("*");
        }


    }
    //Set listeners
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
                if(s.length() == 0){
                    GetNewAdsFilter("*");
                }else{
                    GetNewAdsFilter(s.toString());
                }

            }
        });
    }
    private void GetNewAdsFilter(String input2) {
        int filter;
        String input;
        if(ssCategoryId != -1){
            filter = Constants.FilterBySubSubCategory;
            input = ssCategoryId+"";
        }else if(sCategoryId != -1){
            filter = Constants.FilterBySubCategory;
            input = sCategoryId+"";
        }else{
            filter = Constants.FilterByCategory;
            input = categoryId+"";
        }
        endpoints.FilterAds(input, filter, input2).enqueue(new Callback<ArrayList<AdsModel>>() {
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

                }
            }

            @Override
            public void onFailure(Call<ArrayList<AdsModel>> call, Throwable t) {
                Snackbar.make(binding.adsList, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_SHORT).show();

            }
        });


    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}