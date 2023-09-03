package com.applications.divarapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.applications.divarapp.R;
import com.applications.divarapp.adapters.CityAdapter;
import com.applications.divarapp.databinding.ActivityCityBinding;
import com.applications.divarapp.models.CityModel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityActivity extends AppCompatActivity {

    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);
    //Text change input handler
    TextWatcher textWatcher;
    //List adapter for show items
    private CityAdapter adapter;
    //Where from called
    private String source;
    //Binding
    private ActivityCityBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        AssigningElements();
        Intent intent = getIntent();
        source =  intent.getStringExtra(Constants.Source_Key);
        GetCities();
        SetListeners();
    }

    private void AssigningElements() {

        // Changed listener assign
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchInList(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private void SetListeners(){
        binding.cityList.setOnItemClickListener((parent, view, position, id) -> {
            CityModel model = (CityModel) parent.getItemAtPosition(position);
            SPreferences.setDefaults(Constants.Key_City_SP, model.getId() + ":" + model.getName(), CityActivity.this);
            if(source.equals(Constants.Source_First_City_Selection)) {
                Intent i = new Intent(CityActivity.this, MainActivity.class);
                startActivity(i);
            }else if(source.equals(Constants.Source_Home_City_Selection)) {
                Intent i = new Intent(CityActivity.this, MainActivity.class);
                startActivity(i);
            }
            else if(source.equals(Constants.Source_Ad_City_Selection)){
                CityActivity.this.onBackPressed();
            }
        });
    }
    private void GetCities() {
        endpoints.GetCities(Constants.Secret_Cities).enqueue(new Callback<ArrayList<CityModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CityModel>> call, Response<ArrayList<CityModel>> response) {
                findViewById(R.id.progress_circular).setVisibility(View.GONE);
                if (binding.cityList.getAdapter() == null) {
                    adapter = new CityAdapter(CityActivity.this, R.layout.text_list_item, response.body());
                    binding.cityList.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CityModel>> call, Throwable t) {
                Snackbar.make(binding.content, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.try_again), v -> GetCities()).show();
            }
        });
    }

    public void ViewsActions(View view) {
        switch (view.getId()) {
            case R.id.btn_search:
                //Set Visibility to visible
                Visible(true);
                //Set Visibility to invisible
                Invisible(true);
                //Add text changed listener to input text
                binding.txtSearchInput.addTextChangedListener(textWatcher);
                break;
            case R.id.btn_back:
                //Clean input text of TextInputEditText and remove changed listener
                binding.txtSearchInput.setText("");
                binding.txtSearchInput.removeTextChangedListener(textWatcher);
                // Visibility to invisible
                Visible(false);
                //Set Visibility to visible
                Invisible(false);
                break;
            case R.id.btn_help:

                break;
            default:
                return;
        }
    }
    // Search input text filter
    private void SearchInList(CharSequence input) {
        adapter.getFilter().filter(input);
    }
    // Set visibility to visible
    private void Visible(boolean visible){
        if(visible) {
            binding.txtSearchInputLayout.setVisibility(View.VISIBLE);
            binding.btnBack.setVisibility(View.VISIBLE);
        }else{
            binding.btnSearch.setVisibility(View.VISIBLE);
            binding.btnHelp.setVisibility(View.VISIBLE);
            binding.textView.setVisibility(View.VISIBLE);
        }
    }
    // Set visibility to invisible
    private void Invisible(boolean invisible){
        if(invisible) {
            binding.btnSearch.setVisibility(View.INVISIBLE);
            binding.btnHelp.setVisibility(View.INVISIBLE);
            binding.textView.setVisibility(View.INVISIBLE);
        }else{
            binding.txtSearchInputLayout.setVisibility(View.INVISIBLE);
            binding.btnBack.setVisibility(View.INVISIBLE);
        }
    }
}