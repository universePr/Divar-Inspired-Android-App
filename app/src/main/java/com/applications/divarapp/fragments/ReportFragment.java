package com.applications.divarapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applications.divarapp.R;
import com.applications.divarapp.activities.MainActivity;
import com.applications.divarapp.databinding.FragmentReportBinding;
import com.applications.divarapp.models.DynamicResponseMdoel;
import com.applications.divarapp.models.ReportDataModel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportFragment extends Fragment {

    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);
    //Arguments
    private static final String ARG_ADID = "ad_id";
    //Ad id
    private long adId;
    //Reports
    private boolean ch1 = false;
    private boolean ch2 = false;
    private boolean ch3 = false;
    //Binding
    private FragmentReportBinding binding;

    public static ReportFragment newInstance(long aid) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ADID, aid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReportBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adId = getArguments().getLong(ARG_ADID);
        SetListeners(view);
    }

    private void SetListeners(View view) {
        //Check boxes
        binding.ch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ch1 = isChecked;
        });
        binding.ch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ch2 = isChecked;
        });
        binding.ch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ch3 = isChecked;
        });
        // Buttons
        binding.btnCancel.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        binding.btnBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        binding.btnSubmit.setOnClickListener(v -> {
            if(ch1 || ch2 || ch3) {
                String report = "";
                if (ch1)
                    report += binding.ch1.getText().toString() + "\n";
                if (ch2)
                    report += binding.ch2.getText().toString() + "\n";
                if (ch3)
                    report += binding.ch3.getText().toString() + "\n";

                final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(getContext());
                progressDoalog.setMessage(getString(R.string.submitting_report));
                progressDoalog.setTitle(getString(R.string.upload_report));
                progressDoalog.setCancelable(false);
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                // show it
                progressDoalog.show();
                endpoints.Report(new ReportDataModel(false, true, report, adId, SPreferences.getDefaults(Constants.Key_LoggedInUser_SP, getContext()))).enqueue(new Callback<DynamicResponseMdoel>() {
                    @Override
                    public void onResponse(Call<DynamicResponseMdoel> call, Response<DynamicResponseMdoel> response) {
                        progressDoalog.dismiss();
                        Snackbar.make(binding.appbar, getString(R.string.report_was_successfully_registered), Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent i = new Intent(getActivity(), MainActivity.class);
                                startActivity(i);
                            }
                        }, 800);
                    }

                    @Override
                    public void onFailure(Call<DynamicResponseMdoel> call, Throwable t) {
                        progressDoalog.dismiss();
                        Snackbar.make(binding.appbar, getString(R.string.reporting_is_problematic), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }else{
                Snackbar.make(binding.appbar, getString(R.string.choosing_one_of_the_above_is_mandatory), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}