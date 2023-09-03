package com.applications.divarapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.applications.divarapp.R;
import com.applications.divarapp.activities.AdActivity;
import com.applications.divarapp.activities.ChoseCategoryActivity;
import com.applications.divarapp.activities.MainActivity;
import com.applications.divarapp.adapters.SubCategoryAdapter;
import com.applications.divarapp.models.CategoryModel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;


public class SubCategoryFragment extends Fragment {


    private static final String ARG_SUBNAME = "name";
    private static final String ARG_CATEGORY_ID = "cid";
    private static final String ARG_SCATEGORY_ID = "sid";
    private static final String ARG_ICON = "icon";
    private static final String ARG_ITEMS = "items";
    private static final String ARG_STATE = "state";

    private static ArrayList<CategoryModel.SubCategoryModel> models;
    //Layout elements
    private ListView lv;
    //List adapter for show items
    private ArrayAdapter adapter;
    //State
    private Constants.page_state page_state;

    public SubCategoryFragment() {
        // Required empty public constructor
    }

    public static SubCategoryFragment newInstance(String Name,String IconUrl, ArrayList<CategoryModel.SubCategoryModel> subs,Constants.page_state state, int cid, int sid) {
        SubCategoryFragment fragment = new SubCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SUBNAME, Name);
        args.putString(ARG_ICON, IconUrl);
        args.putInt(ARG_CATEGORY_ID, cid);
        args.putInt(ARG_SCATEGORY_ID, sid);
        args.putParcelableArrayList(ARG_ITEMS, subs);
        if(state == Constants.page_state.AD)
            args.putInt(ARG_STATE, 1);
        else if(state == Constants.page_state.CATEGORIES)
            args.putInt(ARG_STATE, 2);
        else if(state == Constants.page_state.SEARCH){
            args.putInt(ARG_STATE, 3);
        }
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
        return inflater.inflate(R.layout.fragment_sub_category, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Asserting when argument is null
        assert getArguments() != null;
        //Get sending sub category items from first fragment
        models = getArguments().getParcelableArrayList(ARG_ITEMS);
        //Getting state of page
        page_state  = getArguments().getInt(ARG_STATE) == 1 ? Constants.page_state.AD : getArguments().getInt(ARG_STATE) == 2 ?  Constants.page_state.CATEGORIES : Constants.page_state.SEARCH;
        //Finding elements layout
        AssigningElements(view);
        //Set list adapter
        SetListAdapter(view);
        //Set click listener
        SetClickListener(view);
    }

    private void SetClickListener(View view) {
        //Click listener on Show all ads in this category
        (view.findViewById(R.id.show_all)).setOnClickListener(v -> {
            if(page_state == Constants.page_state.SEARCH) {
                int cid = getArguments().getInt(ARG_CATEGORY_ID);
                int scid = getArguments().getInt(ARG_SCATEGORY_ID);
                int sscid = -1;
                ((ChoseCategoryActivity) getActivity()).ChangingFragment(
                        SearchFragment.newInstance(
                                cid,
                                scid,
                                sscid,
                                ""), Constants.Search_Fragment_Tag);
            }else if(page_state == Constants.page_state.CATEGORIES){
                int cid = getArguments().getInt(ARG_CATEGORY_ID);
                int scid = getArguments().getInt(ARG_SCATEGORY_ID);
                int sscid = -1;
                ((MainActivity) getActivity()).ChangingFragment(
                        SearchFragment.newInstance(
                                cid,
                                scid,
                                sscid,
                                ""), Constants.Search_Fragment_Tag);
            }
        });
        //Click listener on list items
        lv.setOnItemClickListener((parent, view1, position, id) -> {
            CategoryModel.SubCategoryModel model = (CategoryModel.SubCategoryModel) parent.getItemAtPosition(position);
            if(IsThisSub(model)){
                if(page_state == Constants.page_state.AD) {
                    ((AdActivity) getActivity()).ChangingFragment(SubCategoryFragment.newInstance(model.getName(), "", model.getSubs(), Constants.page_state.AD, getArguments().getInt(ARG_CATEGORY_ID), model.getId()), Constants.ADSubCategory_Fragment_Tag);
                }else if(page_state == Constants.page_state.CATEGORIES) {
                    ((MainActivity) getActivity()).ChangingFragment(SubCategoryFragment.newInstance(model.getName(), "", model.getSubs(), Constants.page_state.CATEGORIES,getArguments().getInt(ARG_CATEGORY_ID), model.getId()), Constants.SubCategory_Fragment_Tag);
                }else if(page_state == Constants.page_state.SEARCH){
                    ((ChoseCategoryActivity) getActivity()).ChangingFragment(SubCategoryFragment.newInstance(model.getName(), "", model.getSubs(), Constants.page_state.SEARCH,getArguments().getInt(ARG_CATEGORY_ID), model.getId()), Constants.SubCategory_Search_Fragment_Tag);
                }
            }else{
                if(page_state == Constants.page_state.AD) {
                    ((AdActivity) getActivity()).ChangingFragment(AdFragment.newInstance(model.getName(),getArguments().getInt(ARG_CATEGORY_ID),getArguments().getInt(ARG_SCATEGORY_ID) ,model.getId()), Constants.AD_Fragment_Tag);
                }else if(page_state == Constants.page_state.CATEGORIES){
                    int cid = getArguments().getInt(ARG_CATEGORY_ID);
                    int scid = getArguments().getInt(ARG_SCATEGORY_ID);
                    int sscid = model.getId();
                    if(scid == -1) {
                        scid = sscid;
                        sscid = -1;
                    }
                    ((MainActivity) getActivity()).ChangingFragment(
                            SearchFragment.newInstance(
                                    cid,
                                    scid,
                                    sscid,
                                    ""), Constants.Search_Fragment_Tag);
                }else if(page_state == Constants.page_state.SEARCH) {
                    int cid = getArguments().getInt(ARG_CATEGORY_ID);
                    int scid = getArguments().getInt(ARG_SCATEGORY_ID);
                    int sscid = model.getId();
                    if(scid == -1) {
                        scid = sscid;
                        sscid = -1;
                    }
                    ((ChoseCategoryActivity) getActivity()).ChangingFragment(
                            SearchFragment.newInstance(
                                    cid,
                                    scid,
                                    sscid,
                                    ""), Constants.Search_Fragment_Tag);
                }
            }
        });
    }

    private void SetListAdapter(View view) {
        if (lv.getAdapter() == null) {
            adapter = new SubCategoryAdapter(view.getContext(), R.layout.sub_category_list_item, models);
            lv.setAdapter(adapter);
        }
    }

    private void AssigningElements(View view) {
        //Listview
        lv = view.findViewById(R.id.subcategories_list);
        //Set Category name
        ((TextView) view.findViewById(R.id.txt_subname)).setText(getArguments().getString(ARG_SUBNAME));
        //Checking page state for ShowAll item in end of list
        if(page_state == Constants.page_state.AD){
            ((LinearLayout)view.findViewById(R.id.show_all)).setVisibility(View.GONE);
        }else {
            ((TextView) view.findViewById(R.id.show_name)).setText(getString(R.string.all_ads) +" " + getArguments().getString(ARG_SUBNAME));
            //Get category icon
            if (!getArguments().getString(ARG_ICON).isEmpty())
                Glide.with(view)
                        .load(API.getBaseUrlApi() + getArguments().getString(ARG_ICON))
                        .fitCenter()
                        .into(((ImageView) view.findViewById(R.id.img_icon)));

            else {
                ((ImageView) view.findViewById(R.id.img_icon)).setVisibility(View.GONE);
            }
        }
        //Set back press to image button
        ((ImageButton)view.findViewById(R.id.btn_back)).setOnClickListener(v -> {
            if(page_state == Constants.page_state.SEARCH && getArguments().getInt(ARG_SCATEGORY_ID) == -1){
                getActivity().onBackPressed();
            }
            else{
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
    //Is this a subcategory?
    private boolean IsThisSub(CategoryModel.SubCategoryModel model) {
        return model.getSubs() != null && model.getSubs().size() != 0;
    }
}