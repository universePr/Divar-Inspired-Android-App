package com.applications.divarapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applications.divarapp.R;
import com.applications.divarapp.activities.MainActivity;
import com.applications.divarapp.databinding.FragmentNoteBinding;
import com.applications.divarapp.models.BookmarkDataModel;
import com.applications.divarapp.models.DynamicResponseMdoel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NoteFragment extends Fragment {

    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);
    //Arguments
    private static final String ARG_AD_ID = "ad_id";
    private static final String ARG_IS_BOOK = "ad_is_book";
    //Vars
    private long aID;
    private boolean isBook = false;
    //Binding
    private FragmentNoteBinding binding;


    public static NoteFragment newInstance(long aid, boolean isbook) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_AD_ID, aid);
        args.putBoolean(ARG_IS_BOOK, isbook);
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
        binding = FragmentNoteBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        aID = getArguments().getLong(ARG_AD_ID);
        isBook = getArguments().getBoolean(ARG_IS_BOOK);
        SetListeners(view);
    }

    private void SetListeners(View view) {
       binding.btnCancel.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        binding.btnBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        binding.btnSubmit.setOnClickListener(v -> {
            String note = binding.edtNote.getText().toString();
            if(!note.trim().equals("")){
                final ProgressDialog progressDoalog;
                progressDoalog = new ProgressDialog(getContext());
                progressDoalog.setMessage(getString(R.string.submitting_report));
                progressDoalog.setTitle(getString(R.string.upload_report));
                progressDoalog.setCancelable(false);
                progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                endpoints.AdBookmark(new BookmarkDataModel(isBook, true, note, SPreferences.getDefaults(Constants.Key_LoggedInUser_SP,getContext()),
                        aID)).enqueue(new Callback<DynamicResponseMdoel>() {
                    @Override
                    public void onResponse(Call<DynamicResponseMdoel> call, Response<DynamicResponseMdoel> response) {
                        progressDoalog.dismiss();
                        Snackbar.make(binding.appbar, getString(R.string.note_was_successfully_registered), Snackbar.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().onBackPressed();
                            }
                        }, 800);
                    }

                    @Override
                    public void onFailure(Call<DynamicResponseMdoel> call, Throwable t) {
                        progressDoalog.dismiss();
                        Snackbar.make(binding.appbar, getString(R.string.there_problem_with_taking_notes), Snackbar.LENGTH_SHORT).show();
                    }
                });
            }else{
                Snackbar.make(binding.appbar, getString(R.string.enter_the_text_of_the_note), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}