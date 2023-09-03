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

import com.applications.divarapp.R;
import com.applications.divarapp.activities.AdShowActivity;
import com.applications.divarapp.adapters.AdBookmarkListAdapter;
import com.applications.divarapp.databinding.FragmentMybookmarkBinding;
import com.applications.divarapp.models.AdBookMarkModelResponse;
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

public class MyBookmarkFragment extends Fragment {
    private FragmentMybookmarkBinding binding;

    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);
    //Ads list
    private final ArrayList<AdBookMarkModelResponse> adsitems = new ArrayList<>();
    private String userPhone;
    //Ad adapter
    private AdBookmarkListAdapter adapterAd;

    public static MyBookmarkFragment newInstance() {
        MyBookmarkFragment fragment = new MyBookmarkFragment();
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
        binding = FragmentMybookmarkBinding.inflate(inflater, container, false );
        View view = binding.getRoot();
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userPhone = SPreferences.getDefaults(Constants.Key_LoggedInUser_SP, getContext());
        AssigningElements(view);
    }

    private void AssigningElements(View view) {
        binding.btnBack.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        GetBookMarks(userPhone);
    }
    //Getting ad in user location
    private void GetBookMarks(String phone) {
        endpoints.GetMyBookMarks(phone).enqueue(new Callback<ArrayList<AdBookMarkModelResponse>>() {
            @Override
            public void onResponse(Call<ArrayList<AdBookMarkModelResponse>> call, Response<ArrayList<AdBookMarkModelResponse>> response) {

                if(response.body() != null){
                    binding.adsList.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapterAd = new AdBookmarkListAdapter(response.body(),getContext(), item -> {
                        Intent i = new Intent(getActivity(), AdShowActivity.class);
                        AdsModel ad = new AdsModel();
                        ad.setChatCollectionName(item.getChatCollectionName());
                        ad.setHide(item.isHide());
                        ad.setMembership(item.getMembership());
                        ad.setSCategoryName(item.getSCategoryName());
                        ad.setSSCategoryName(item.getSSCategoryName());
                        ad.setSCategoryName(item.getCategoryName());
                        ad.setAdId(item.getAdId());
                        ad.setAreaId(item.getAreaId());
                        ad.setTitle(item.getTitle());
                        ad.setCanMessage(item.isCanMessage());
                        ad.setCityId(item.getCityId());
                        ad.setSubSubCategoryId(item.getSubSubCategoryId());
                        ad.setSubCategoryId(item.getSubCategoryId());
                        ad.setStatus(item.isStatus());
                        ad.setShowPhon(item.isShowPhone());
                        ad.setImageUrls(item.getImageUrls());
                        ad.setFinalPrice(item.getFinalPrice());
                        ad.setPhone(item.getPhone());
                        ad.setDescription(item.getDescription());
                        ad.setDateTime(item.getDateTime());
                        ad.setContainImage(item.isContainImage());
                        ad.setCityName(item.getCityName());
                        ad.setCategoryName(item.getCategoryName());
                        ad.setCategoryId(item.getCategoryId());
                        i.putExtra(Constants.Key_Extra_AId, ad);
                        startActivity(i);
                    });
                    binding.adsList.setAdapter(adapterAd);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<AdBookMarkModelResponse>> call, Throwable t) {
                Snackbar.make(binding.content, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.try_again), v -> GetBookMarks(userPhone))
                        .show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        GetBookMarks(userPhone);
    }
}