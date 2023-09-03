package com.applications.divarapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.applications.divarapp.R;
import com.applications.divarapp.activities.MainActivity;
import com.applications.divarapp.adapters.CategoryAdapter;
import com.applications.divarapp.databinding.FragmentCategoriesBinding;
import com.applications.divarapp.models.CategoryModel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoriesFragment extends Fragment {

    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);

    //List adapter for show items
    private CategoryAdapter adapter;
    //Binding
    private FragmentCategoriesBinding binding;
    public CategoriesFragment() {
        // Required empty public constructor
    }

    public static CategoriesFragment newInstance() {
        CategoriesFragment fragment = new CategoriesFragment();
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
        binding = FragmentCategoriesBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GetCategories(view);
        SetListeners(view);
    }

    private void SetListeners(View v){
        binding.categoriesList.setOnItemClickListener((parent, view, position, id) ->{
            CategoryModel model = (CategoryModel) parent.getItemAtPosition(position);
            ((MainActivity)getActivity()).ChangingFragment(SubCategoryFragment.newInstance(model.getName(),model.getIconUrl(), model.getSubs(), Constants.page_state.CATEGORIES, model.getId(),-1), Constants.SubCategory_Fragment_Tag);
        });
    }
    private void GetCategories(View view) {
        endpoints.GetCategories(Constants.Secret_Categories).enqueue(new Callback<ArrayList<CategoryModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryModel>> call, Response<ArrayList<CategoryModel>> response) {
                view.findViewById(R.id.progress_circular).setVisibility(View.GONE);
                if (binding.categoriesList.getAdapter() == null) {
                    adapter = new CategoryAdapter(getContext(), R.layout.category_list_item, response.body());
                    binding.categoriesList.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryModel>> call, Throwable t) {
                try {
                    Snackbar.make(binding.appbar, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.try_again), v -> GetCategories(view)).show();
                }catch (Exception e){
                    Toast.makeText(getContext(), getString(R.string.communication_problem_has_occurred), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}