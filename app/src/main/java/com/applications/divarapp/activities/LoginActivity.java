package com.applications.divarapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import com.applications.divarapp.R;
import com.applications.divarapp.databinding.ActivityLoginBinding;
import com.applications.divarapp.models.ResponseModel;
import com.applications.divarapp.models.SignupDataModel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.Md5;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.material.snackbar.Snackbar;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);

    //State for send sms code
    private boolean phoneIsOK = false;
    private boolean sendCodeIsOK = true;

    //Getting code
    private int code;
    //Phone
    private String phone = "";
    //Binding
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SetListeners();
    }

    public void ViewsActions(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                if(phoneIsOK && sendCodeIsOK) {
                    if(phone.equals(""))
                        phone = binding.txtPhoneInput.getText().toString();
                    PostVCode();
                }
                break;
            case R.id.btn_check:
                if(binding.txtPhoneInput.getText().toString().equals(""+code)){
                    PostSignup();
                }
                break;
            case R.id.btn_back:
                StartLayout();
                break;
        }
    }
    //Send post request for get code
    private void PostVCode(){
        endpoints.PostVcode(Md5.getMd5Hash(phone), phone).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.body() != null) {
                    Snackbar.make(binding.content, getString(R.string.successfully_sent_code) + response.body().getCode(), Snackbar.LENGTH_INDEFINITE).show();
                    //Save code
                    code = response.body().getCode();
                    //Layout control
                    Visible(true);
                    Invisible(true);
                    ChangeLayout();
                    //Start timer to send again request
                    sendCodeIsOK = false;
                    TimerHandler();
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Snackbar.make(binding.content,getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    //Send post request for signup user
    private void PostSignup(){
        endpoints.PostSignup(Md5.getMd5Hash(phone+code), new SignupDataModel(code, phone,UserCityId())).enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.body() != null){
                    SPreferences.setDefaults(Constants.Key_LoggedInUser_SP, phone, LoginActivity.this);
                    SPreferences.setDefaults(Constants.Key_LoggedInUserCode_SP, ""+code, LoginActivity.this);
                    SPreferences.setDefaults(Constants.Key_LoggedInUserToken_SP, response.body().getToken(), LoginActivity.this);
                    Snackbar.make(binding.content, getString(R.string.you_have_successfully_logged_in), Snackbar.LENGTH_SHORT).show();
                    LoginActivity.this.onBackPressed();
                }

            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Snackbar.make(binding.content,getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    //Get user city id from shared preferences
    private int UserCityId(){
        Map<String, Object> map_sp = SPreferences.hasKey(Constants.Key_City_SP, this);
        if((boolean) map_sp.get("1")){
            return Integer.parseInt(((String) (map_sp.get("2"))).split(":")[0]);
        }else {
            Intent i = new Intent(LoginActivity.this,FirstActivity.class);
            startActivity(i);
        }
        return -1;
    }
    //Set listeners
    private void SetListeners(){
        //check phone number / correct input
        binding.txtPhoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().matches("(\\+98|0)?9\\d{9}")) {
                    phoneIsOK = true;
                    hideKeyboard(LoginActivity.this);

                } else {
                    phoneIsOK = false;

                }
            }
        });
    }
    //Hide keyboard when user type phone number
    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    // Set visibility to visible
    private void Visible(boolean visible){
        if(visible) {
            binding.btnCheck.setVisibility(View.VISIBLE);
            binding.btnBack.setVisibility(View.VISIBLE);
            binding.txtTimer.setVisibility(View.VISIBLE);

        }else{
            binding.btnSend.setVisibility(View.VISIBLE);
        }
    }
    // Set visibility to invisible
    private void Invisible(boolean invisible){
        if(invisible) {
            binding.btnSend.setVisibility(View.INVISIBLE);
        }else{
            binding.txtTimer.setVisibility(View.INVISIBLE);
            binding.btnCheck.setVisibility(View.INVISIBLE);
        }
    }
    //Timer for new request to vcode
    private void TimerHandler(){
        new CountDownTimer(20000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.txtTimer.setText(getString(R.string.request_again_code) + millisUntilFinished / 1000 + " )");
            }

            public void onFinish() {
                phoneIsOK = true;
                sendCodeIsOK = true;
                Visible(false);
                Invisible(false);
            }

        }.start();

    }

    //Clear input phone and set new strings
    private void ChangeLayout(){
        binding.pleaseEnterYourPhone.setText(getString(R.string.enter_Code));
        binding.msg.setText(getString(R.string.please_give_confirmation_code_the_number) + " " + phone + " " + getString(R.string.enter_the_text_message));
        binding.txtPhoneInput.setText("");
        binding.btnSend.setText(getString(R.string.resend));
    }
    //Clear input phone and return layout to start
    private void StartLayout(){
        binding.pleaseEnterYourPhone.setText(getString(R.string.please_enter_your_phone));
        binding.msg.setText(getString(R.string.to_use_the_features_of_application_please_enter_your_mobile_number));
        binding.txtPhoneInput.setText(phone);
        binding.btnSend.setText(getString(R.string.next));
        phone = "";
        binding.btnBack.setVisibility(View.GONE);
        binding.btnCheck.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}