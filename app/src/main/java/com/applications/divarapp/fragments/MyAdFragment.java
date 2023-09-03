package com.applications.divarapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.applications.divarapp.R;
import com.applications.divarapp.activities.AdShowActivity;
import com.applications.divarapp.adapters.AdListAdapter;
import com.applications.divarapp.adapters.MyAdListAdapter;
import com.applications.divarapp.databinding.FragmentMyAdsBinding;
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

public class MyAdFragment extends Fragment {

    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);
    //Ad adapter
    private MyAdListAdapter adapterAd;
    //Binding
    private FragmentMyAdsBinding binding;

    public static MyAdFragment newInstance() {
        MyAdFragment fragment = new MyAdFragment();
        return fragment;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyAdsBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AssigningElements(view);
    }

    private void AssigningElements(View view) {
        binding.btnBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        GetMyAdsFilter(SPreferences.getDefaults(Constants.Key_LoggedInUser_SP, getContext()), Constants.FilterByUser, "temp");
    }
    //Getting ad in user location
    private void GetMyAdsFilter(String input , int filter, String input2) {
        endpoints.FilterAds(input, filter,input2).enqueue(new Callback<ArrayList<AdsModel>>() {
            @Override
            public void onResponse(Call<ArrayList<AdsModel>> call, Response<ArrayList<AdsModel>> response) {
                if(response.body() != null){

                    binding.adsList.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterAd = new MyAdListAdapter(response.body(),getContext(), item -> {
                        Intent i = new Intent(getActivity(), AdShowActivity.class);
                        i.putExtra(Constants.Key_Extra_AId, item);
                        startActivity(i);
                    });
                    binding.adsList.setAdapter(adapterAd);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AdsModel>> call, Throwable t) {
                Snackbar.make(binding.appbar, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.try_again), v -> GetMyAdsFilter(SPreferences.getDefaults(Constants.Key_LoggedInUser_SP, getContext()), Constants.FilterByUser, "temp")).show();

            }
        });


    }
}
