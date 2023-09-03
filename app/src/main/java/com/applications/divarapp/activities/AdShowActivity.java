package com.applications.divarapp.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.applications.divarapp.R;
import com.applications.divarapp.adapters.SliderAdapter;
import com.applications.divarapp.databinding.ActivityShowAdBinding;
import com.applications.divarapp.fragments.NoteFragment;
import com.applications.divarapp.fragments.ReportFragment;
import com.applications.divarapp.helpers.DBHelper;
import com.applications.divarapp.models.AdsModel;
import com.applications.divarapp.models.BookmarkDataModel;
import com.applications.divarapp.models.BookmarkModel;
import com.applications.divarapp.models.ChatResponseCheck;
import com.applications.divarapp.models.CheckChatDataModel;
import com.applications.divarapp.models.DynamicResponseMdoel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdShowActivity extends AppCompatActivity {
    private ActivityShowAdBinding binding;
    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);
    //Data model
    private AdsModel model;
    //Fragment transaction
    private FragmentTransaction fragmentTransaction;
    //Bookmark model
    private BookmarkModel bookmark;

    private String userPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowAdBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        Intent i = getIntent();
        model = (AdsModel) i.getSerializableExtra(Constants.Key_Extra_AId);
        userPhone = SPreferences.getDefaults(Constants.Key_LoggedInUser_SP,this);
        DBHelper db = new DBHelper(getApplicationContext());
        if(!db.isExistAd(model.getAdId()+"")) {
            db.addRecentlyView(model.getAdId() + "", model.getTitle());
        }
        AssigningElements();
        SetListeners();
        CheckBookMark();
    }

    private void CheckBookMark() {
        endpoints.BookCheck(SPreferences.getDefaults(Constants.Key_LoggedInUser_SP, this),model.getAdId())
                .enqueue(new Callback<BookmarkModel>() {
                    @Override
                    public void onResponse(Call<BookmarkModel> call, Response<BookmarkModel> response) {
                        if(response.body() != null){
                            bookmark = response.body();
                            if(bookmark.isBookMark()) {
                                binding.btnBookmark.setColorFilter(getResources().getColor(R.color.remove_danger));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BookmarkModel> call, Throwable t) {
                        Snackbar.make(binding.lay, getString(R.string.communication_problem_has_occurred), Snackbar.LENGTH_INDEFINITE)
                                .setAction(getString(R.string.try_again), v -> CheckBookMark()).show();
                    }
                });
    }
    //Checking user logged in
    private boolean UserLoggedIn() {
        Map<String, Object> map_sp = SPreferences.hasKey(Constants.Key_LoggedInUser_SP, this);
        return (boolean) map_sp.get("1");
    }
    private void CheckUserInChat(String userPhone){

        endpoints.CheckUserInChat(new CheckChatDataModel(model.getAdId(), userPhone)).enqueue(new Callback<ChatResponseCheck>() {
            @Override
            public void onResponse(Call<ChatResponseCheck> call, Response<ChatResponseCheck> response) {
                if(response != null) {
                    if (UserLoggedIn()) {
                        if (!response.body().isResponse()) {

                            binding.btnChat.setOnClickListener(v -> {

                                Intent i = new Intent(AdShowActivity.this, TalkActivity.class);
                                i.putExtra("ad_title", model.getTitle());
                                i.putExtra("ad_id", model.getAdId());
                                i.putExtra("ad_col", model.getChatCollectionName());
                                i.putExtra("role", "customer");
                                i.putExtra("newChat", true);
                                startActivity(i);

                            });
                        } else {
                            binding.btnChat.setOnClickListener(v -> {
                                Intent i = new Intent(AdShowActivity.this, TalkActivity.class);
                                i.putExtra("ad_title", model.getTitle());
                                i.putExtra("ad_id", model.getAdId());
                                i.putExtra("ad_col", model.getChatCollectionName());
                                i.putExtra("role", "customer");
                                i.putExtra("newChat", false);
                                startActivity(i);
                            });
                        }
                    }else{
                        binding.btnChat.setOnClickListener(v ->{
                            Intent i = new Intent(AdShowActivity.this,LoginActivity.class);
                            startActivity(i);
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ChatResponseCheck> call, Throwable t) {

            }
        });
    }
    private void SetListeners() {
        //Buttons

        if(model.isCanMessage() && !model.getPhone().equals(userPhone)) {
            CheckUserInChat(userPhone);

        }else{
            binding.btnChat.setVisibility(View.INVISIBLE);
        }
        if(model.isShowPhon()){
            binding.btnShowPhone.setOnClickListener(v -> {
                showCallInfoBottomSheetDialog();
            });
        }else{
            binding.btnShowPhone.setVisibility(View.INVISIBLE);
        }
        binding.btnBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnMenu.setOnClickListener(v -> {
            showNoteBottomSheetDialog();
        });
        binding.btnBookmark.setOnClickListener(v -> {
            endpoints.AdBookmark(new BookmarkDataModel(!bookmark.isBookMark(), bookmark.isNoteCheck(),
                    bookmark.getNote(), SPreferences.getDefaults(Constants.Key_LoggedInUser_SP, AdShowActivity.this),
                    model.getAdId())).enqueue(new Callback<DynamicResponseMdoel>() {
                @Override
                public void onResponse(Call<DynamicResponseMdoel> call, Response<DynamicResponseMdoel> response) {
                    if(response.body() != null){
                        if(response.body().getCode() == 200){
                            bookmark.setBookMark(!bookmark.isBookMark());
                            if(bookmark.isBookMark()) {
                                binding.btnBookmark.setColorFilter(getResources().getColor(R.color.remove_danger));
                                Snackbar.make(binding.lay, getString(R.string.the_ad_was_marked), Snackbar.LENGTH_SHORT).show();

                            }else{
                                binding.btnBookmark.setColorFilter(getResources().getColor(R.color.white));
                                Snackbar.make(binding.lay, getString(R.string.the_ad_was_removed), Snackbar.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<DynamicResponseMdoel> call, Throwable t) {
                    Snackbar.make(binding.lay, getString(R.string.ad_not_shown_check_your_internet), Snackbar.LENGTH_SHORT).show();
                }
            });
        });


        //Layouts
        binding.aboutSection.setOnClickListener(v -> {
            Intent i = new Intent(AdShowActivity.this, AboutAdOwnerActivity.class);
            i.putExtra("phone", model.getPhone());
            startActivity(i);
        });
        binding.reportSection.setOnClickListener(v -> {
            binding.content.setVisibility(View.GONE);
            ChangingFragment(ReportFragment.newInstance(model.getAdId()), Constants.Report_Fragment_Tag);
        });
    }
    //For transaction current fragment to input
    private void TransactionFragment(Fragment fragment, String Tag){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.page_frame,fragment, Tag);
        fragmentTransaction.addToBackStack(Tag);
        fragmentTransaction.commit();
    }
    //This calling by fragment
    public void ChangingFragment(Fragment fragment, String Tag){
        TransactionFragment(fragment, Tag);
    }

    private void AssigningElements() {
        if(model.isContainImage()) {

            SliderAdapter sliderAdapter = new SliderAdapter(getBaseContext(), model.getImageUrls());
            binding.imageSlider.setSliderAdapter(sliderAdapter);
            binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.SWAP); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            binding.imageSlider.setSliderTransformAnimation(SliderAnimations.FADETRANSFORMATION);
            binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            binding.imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
            binding.imageSlider.startAutoCycle();
        }

        binding.adTitle.setText(model.getTitle());
        binding.adDesc.setText( model.getDateTime() + " "+getString(R.string.in)+" " + model.getCityName());
        binding.txtCat.setText(model.getSCategoryName() + (!model.getSSCategoryName().equals("empty") ?  "/" + model.getSSCategoryName() : ""));
        binding.txtPrice.setText(model.getFinalPrice() + " " + getString(R.string.dollar));
        binding.txtStatus.setText(model.isStatus()? getString(R.string.st_quick_sale) : getString(R.string.st_normal));
        binding.txtDesc.setText(model.getDescription());

        binding.translateSection.setOnClickListener(view -> {
            showTranslateBottomSheetDialog();
        });
    }
    private void showCallInfoBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_call_info_dialog_layout);

        LinearLayout call = bottomSheetDialog.findViewById(R.id.call);
        LinearLayout msg = bottomSheetDialog.findViewById(R.id.msg);
        ((TextView)call.findViewById(R.id.txt_call)).setText(getString(R.string.phone_call_to)+" "+model.getPhone());
        ((TextView)msg.findViewById(R.id.txt_msg)).setText(getString(R.string.send_sms_to)+" "+ model.getPhone());

        call.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+model.getPhone()));
            startActivity(callIntent);
        });
        msg.setOnClickListener(v -> {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:"+model.getPhone()));
            startActivity(smsIntent);
        });

        bottomSheetDialog.show();
    }
    private void showTranslateBottomSheetDialog(){

        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient();
        languageIdentifier.identifyLanguage(binding.adTitle.getText().toString())
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode.equals("und")) {
                                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.translation_failed), Toast.LENGTH_SHORT).show();
                                } else {
                                    if(languageCode.equals("en")){
                                        String lang = SPreferences.getDefaults("lang", getApplicationContext());

                                        if(lang.equals("es")){
                                            binding.langProgress.setVisibility(View.VISIBLE);
                                            MainActivity.englishSpanishTranslator.translate(binding.adDesc.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.langProgress.setVisibility(View.INVISIBLE);
                                                        binding.adDesc.setText(runnable);
                                                    });
                                            MainActivity.englishSpanishTranslator.translate(binding.txtDesc.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.txtDesc.setText(runnable);
                                                    });
                                            MainActivity.englishSpanishTranslator.translate(binding.txtCat.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.txtCat.setText(runnable);
                                                    });
                                            MainActivity.englishSpanishTranslator.translate(binding.adTitle.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.adTitle.setText(runnable);
                                                    });
                                            MainActivity.englishSpanishTranslator.translate(binding.adTitle.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.adTitle.setText(runnable);
                                                    });
                                            MainActivity.englishSpanishTranslator.translate(binding.txtStatus.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.txtStatus.setText(runnable);
                                                    });
                                            MainActivity.englishSpanishTranslator.translate(binding.txtPrice.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.txtPrice.setText(runnable);
                                                    });

                                        }else if(lang.equals("fa")){
                                            binding.langProgress.setVisibility(View.VISIBLE);
                                            MainActivity.englishPersianTranslator.translate(binding.adDesc.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.adDesc.setText(runnable);
                                                        binding.langProgress.setVisibility(View.INVISIBLE);
                                                    });
                                            MainActivity.englishPersianTranslator.translate(binding.txtDesc.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.txtDesc.setText(runnable);
                                                    });
                                            MainActivity.englishPersianTranslator.translate(binding.txtCat.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.txtCat.setText(runnable);
                                                    });
                                            MainActivity.englishPersianTranslator.translate(binding.adTitle.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.adTitle.setText(runnable);
                                                    });
                                            MainActivity.englishPersianTranslator.translate(binding.adTitle.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.adTitle.setText(runnable);
                                                    });
                                            MainActivity.englishPersianTranslator.translate(binding.txtStatus.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.txtStatus.setText(runnable);
                                                    });
                                            MainActivity.englishPersianTranslator.translate(binding.txtPrice.getText().toString())
                                                    .addOnSuccessListener(runnable -> {
                                                        binding.txtPrice.setText(runnable);
                                                    });
                                        }

                                    }
                                    else   if(languageCode.equals("es")){

                                        binding.langProgress.setVisibility(View.VISIBLE);
                                        MainActivity.spanishEnglishTranslator.translate(binding.adDesc.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.langProgress.setVisibility(View.INVISIBLE);
                                                    binding.adDesc.setText(runnable);
                                                });
                                        MainActivity.spanishEnglishTranslator.translate(binding.txtDesc.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.txtDesc.setText(runnable);
                                                });
                                        MainActivity.spanishEnglishTranslator.translate(binding.txtCat.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.txtCat.setText(runnable);
                                                });
                                        MainActivity.spanishEnglishTranslator.translate(binding.adTitle.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.adTitle.setText(runnable);
                                                });
                                        MainActivity.spanishEnglishTranslator.translate(binding.adTitle.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.adTitle.setText(runnable);
                                                });
                                        MainActivity.spanishEnglishTranslator.translate(binding.txtStatus.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.txtStatus.setText(runnable);
                                                });
                                        MainActivity.spanishEnglishTranslator.translate(binding.txtPrice.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.txtPrice.setText(runnable);
                                                });

                                    }else if(languageIdentifier.equals("fa")){
                                        binding.langProgress.setVisibility(View.VISIBLE);
                                        MainActivity.persianEnglishTranslator.translate(binding.adDesc.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.adDesc.setText(runnable);
                                                    binding.langProgress.setVisibility(View.INVISIBLE);
                                                });
                                        MainActivity.persianEnglishTranslator.translate(binding.txtDesc.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.txtDesc.setText(runnable);
                                                });
                                        MainActivity.persianEnglishTranslator.translate(binding.txtCat.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.txtCat.setText(runnable);
                                                });
                                        MainActivity.persianEnglishTranslator.translate(binding.adTitle.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.adTitle.setText(runnable);
                                                });
                                        MainActivity.persianEnglishTranslator.translate(binding.adTitle.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.adTitle.setText(runnable);
                                                });
                                        MainActivity.persianEnglishTranslator.translate(binding.txtStatus.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.txtStatus.setText(runnable);
                                                });
                                        MainActivity.persianEnglishTranslator.translate(binding.txtPrice.getText().toString())
                                                .addOnSuccessListener(runnable -> {
                                                    binding.txtPrice.setText(runnable);
                                                });
                                    }

                                }

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.translation_failed), Toast.LENGTH_SHORT).show();
                            }
                        });

    }
    private void showNoteBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_note_dialog_layout);

        LinearLayout note = bottomSheetDialog.findViewById(R.id.note);
        LinearLayout share = bottomSheetDialog.findViewById(R.id.share);

        note.setOnClickListener(v -> {
            binding.content.setVisibility(View.GONE);
            ChangingFragment(NoteFragment.newInstance(model.getAdId(), bookmark.isBookMark()), Constants.NOTE_Fragment_Tag);
            bottomSheetDialog.cancel();
        });
        share.setOnClickListener(v -> {
            Snackbar.make(binding.lay, getString(R.string.share_ad_coming_soon), Snackbar.LENGTH_SHORT).show();
            bottomSheetDialog.cancel();
        });

        bottomSheetDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        CheckUserInChat(userPhone);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        binding.content.setVisibility(View.VISIBLE);
        CheckBookMark();
    }
}