package com.applications.divarapp.fragments;

import android.os.Bundle;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applications.divarapp.R;
import com.applications.divarapp.activities.MainActivity;
import com.applications.divarapp.databinding.FragmentSettingBinding;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.LanguageManager;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;
import java.util.Objects;

public class SettingFragment extends Fragment {

    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);
    //Binding
    private FragmentSettingBinding binding;

    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
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
        binding = FragmentSettingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AssigningElements(view);
        SetListeners(view);
    }

    private void SetListeners(View view) {
        binding.btnLang.setOnClickListener(view1 -> {
            showTranslateBottomSheetDialog();
        });
    }
    private void showTranslateBottomSheetDialog(){

        LanguageManager languageManager = new LanguageManager(getContext());


        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_translate_dialog_layout);

        LinearLayout english = bottomSheetDialog.findViewById(R.id.english);
        LinearLayout spanish = bottomSheetDialog.findViewById(R.id.spanish);
        LinearLayout persian = bottomSheetDialog.findViewById(R.id.persian);


        english.setOnClickListener(v -> {
            languageManager.updateResource("en");
            bottomSheetDialog.cancel();
            ((MainActivity)getActivity()).enableBottomBar(false);
            MenuItem menuItem = ((MainActivity)getActivity()).getNavigationView().getMenu().findItem(R.id.home);
            menuItem.setChecked(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    requireActivity().recreate();
                    ((MainActivity)getActivity()).enableBottomBar(true);
                }
            },1000);
        });
        spanish.setOnClickListener(view -> {
            languageManager.updateResource("es");
            bottomSheetDialog.cancel();
            ((MainActivity)getActivity()).enableBottomBar(false);
            MenuItem menuItem = ((MainActivity)getActivity()).getNavigationView().getMenu().findItem(R.id.home);
            menuItem.setChecked(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //requireActivity().recreate();
                    getActivity().recreate();
                    ((MainActivity)getActivity()).enableBottomBar(true);
                }
            },1000);
        });
        persian.setOnClickListener(v -> {
            languageManager.updateResource("fa");
            bottomSheetDialog.cancel();
            ((MainActivity)getActivity()).enableBottomBar(false);
            MenuItem menuItem = ((MainActivity)getActivity()).getNavigationView().getMenu().findItem(R.id.home);
            menuItem.setChecked(true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    getActivity().recreate();
                    ((MainActivity)getActivity()).enableBottomBar(true);
                }
            },1000);
        });

        bottomSheetDialog.show();
    }
    private void AssigningElements(View view) {
        if(UserLoggedIn()){
            binding.txtUser.setText(getString(R.string.you_with_the_number) + " "+
                    SPreferences.getDefaults(Constants.Key_LoggedInUser_SP, getContext())+ " "+ getString(R.string.you_are_logged_in_and_you_are_viewing_the_ads_registered_with_this_number));
            binding.btnLogout.setOnClickListener(v -> {
                SPreferences.logoutUser(Constants.Key_LoggedInUser_SP, getContext());
                SPreferences.logoutUser(Constants.Key_LoggedInUserToken_SP, getContext());
                SPreferences.logoutUser(Constants.Key_LoggedInUserCode_SP, getContext());

                binding.logoutSection.setVisibility(View.GONE);
                binding.personalSettingSection.setVisibility(View.GONE);

                Snackbar.make(binding.appbar, getString(R.string.you_have_successfully_logged_out_of_your_account), Snackbar.LENGTH_SHORT).show();
            });
            binding.personalSettingSection.setOnClickListener(v -> {
                ((MainActivity)getActivity()).ChangingFragment(MyAdFragment.newInstance(), Constants.My_Ads_Fragment_Tag);
            });
            binding.bookmarks.setOnClickListener(v -> {
                ((MainActivity)getActivity()).ChangingFragment(MyBookmarkFragment.newInstance(), Constants.My_Ad_Bookmarks_Fragment_Tag);
            });
        }else{
            binding.logoutSection.setVisibility(View.GONE);
            binding.personalSettingSection.setVisibility(View.GONE);
        }
    }

    //Checking user logged in
    private boolean UserLoggedIn() {
        Map<String, Object> map_sp = SPreferences.hasKey(Constants.Key_LoggedInUser_SP, getContext());
        return (boolean) map_sp.get("1");
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}