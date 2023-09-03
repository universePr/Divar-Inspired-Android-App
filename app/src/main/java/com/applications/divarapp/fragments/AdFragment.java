package com.applications.divarapp.fragments;

import static android.app.Activity.RESULT_OK;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import com.applications.divarapp.R;
import com.applications.divarapp.activities.AdActivity;
import com.applications.divarapp.activities.CityActivity;
import com.applications.divarapp.databinding.FragmentAdBinding;
import com.applications.divarapp.models.DynamicResponseMdoel;
import com.applications.divarapp.network.API;
import com.applications.divarapp.network.Endpoints;
import com.applications.divarapp.utils.Constants;
import com.applications.divarapp.utils.SPreferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdFragment extends Fragment  {

    //Argument keys
    private static final String ARG_CATEGORY_NAME = "cname";
    private static final String ARG_CATEGORY_ID = "cid";
    private static final String ARG_SCATEGORY_ID = "sid";
    private static final String ARG_SSCATEGORY_ID = "ssid";
    //Reference to API endpoints
    private final Endpoints endpoints = API.getRetrofitInstance().create(Endpoints.class);

    //Selected category Id
    private int CategoryId;

    //Array of image user selected -> 6 Image
    private final File[] imgsInput = new File[]{null, null, null, null, null,null};
    /*
     Ads parameters
    */
    private boolean ContainImage = false;
    //False is not forced ad
    private boolean Status = false;
    //Others can chat with owner ad
    private boolean CanMessage = false;
    //Others can see owner number
    private boolean ShowPhone = true;
    //Ad price
    private double FinalPrice = -1;
    //City id
    private int adCityId = -1;
    //City name
    private String adCityName = "";
    //Files
    File temp1;
    File temp2;
    File temp3;
    File temp4;
    File temp5;
    File temp6;
    //Picture index
    private int pictureIndex = -1;

    //Binding
    private FragmentAdBinding binding;
    private ProgressDialog progressDoalog;

    public static AdFragment newInstance(String name, int cid, int sid, int ssid) {
        AdFragment fragment = new AdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, name);
        args.putInt(ARG_CATEGORY_ID, cid);
        if(sid == -1) {
            args.putInt(ARG_SCATEGORY_ID, ssid);
            args.putInt(ARG_SSCATEGORY_ID, -1);
        }
        else {
            args.putInt(ARG_SCATEGORY_ID, sid);
            args.putInt(ARG_SSCATEGORY_ID, ssid);
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
        binding = FragmentAdBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //imageInputHelper.setImageActionListener(this);
        adCityId =  Integer.parseInt(SPreferences.getDefaults(Constants.Key_City_SP, getContext()).split(":")[0]);
        adCityName =  SPreferences.getDefaults(Constants.Key_City_SP, getContext()).split(":")[1];


        AssigningElements(view);
        SetListeners(view);
    }

    @Override
    public void onResume() {
        adCityId =  Integer.parseInt(SPreferences.getDefaults(Constants.Key_City_SP, getContext()).split(":")[0]);
        adCityName =  SPreferences.getDefaults(Constants.Key_City_SP, getContext()).split(":")[1];
        //City name
        binding.cityNameTxt.setText(adCityName);
        super.onResume();
    }



    private void AssigningElements(View view) {
        //Category name
        binding.categoryName.setText(getArguments().getString(ARG_CATEGORY_NAME));
        //City name
        binding.cityNameTxt.setText(adCityName);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void SetListeners(View v) {
        binding.helpSection.setOnClickListener(v1 -> {
            try {
                Snackbar.make(binding.appbar, getString(R.string.you_have_selected_the_ad_registration_guide), Snackbar.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(getContext(), getString(R.string.you_have_selected_the_ad_registration_guide), Toast.LENGTH_SHORT).show();
            }
        });
        //Button send ad action
        binding.btnNext1.setOnClickListener(v13 -> {
                    if(!binding.txtSubjectInput.getText().toString().isEmpty()  && !binding.txtSubjectInput.getText().toString().equals("")
                            && !binding.txtDescInput.getText().toString().isEmpty() && !binding.txtDescInput.getText().toString().equals("") ) {
                        GoToLayout2(v);
                    }else{
                        try {
                            Snackbar.make(binding.appbar, getString(R.string.enter_a_title_and_description), Snackbar.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(getContext(), getString(R.string.enter_a_title_and_description), Toast.LENGTH_SHORT).show();
                        }
                    }

        });
        binding.btnBack.setOnClickListener(v15 -> {
            GoToLayout1(v);
        });
        binding.btnBack2.setOnClickListener(v15 -> {
            GoToLayout2from3(v);
        });
        binding.btnSubmitMap.setOnClickListener(v1 -> {
            GoToLayout2from3(v);
        });
        binding.btnNext2.setOnClickListener(v1 -> {
            if(!CanMessage && !ShowPhone){
                try {
                    Snackbar.make(binding.appbar, getString(R.string.one_the_display_number_chat_ads_must_be_active), Snackbar.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getContext(), getString(R.string.one_the_display_number_chat_ads_must_be_active), Toast.LENGTH_SHORT).show();
                }
            }else {
                PostAd(binding.txtSubjectInput.getText().toString(), binding.txtDescInput.getText().toString());
            }
        });
        //Cancel buttons
        binding.btnCancel.setOnClickListener(v12 -> {
            getActivity().getSupportFragmentManager().popBackStack();
        });
        binding.btnGoToFirst.setOnClickListener(V13 -> {
            ((AdActivity) getActivity()).ChangingFragment(SelecteCategoriesFragment.newInstance(), Constants.ADCategory_Fragment_Tag);
        });
        //Selecting images from gallery
        binding.picture1.setOnClickListener(v14 -> {
            selectPicture(1);
        });
        binding.picture2.setOnClickListener(v14 -> {
            selectPicture(2);
        });
        binding.picture3.setOnClickListener(v14 -> {
            selectPicture(3);
        });
        binding.picture4.setOnClickListener(v14 -> {
            selectPicture(4);
        });
        binding.picture5.setOnClickListener(v14 -> {
            selectPicture(5);
        });
        binding.picture6.setOnClickListener(v14 -> {
            selectPicture(6);
        });
        //Clear buttons
        binding.remove1.setOnClickListener(v14 -> {
            clearImage(R.id.icon1, R.mipmap.ic_camera, R.id.remove1, 1);
        });
        binding.remove2.setOnClickListener(v14 -> {
            clearImage(R.id.icon2, R.mipmap.ic_picture, R.id.remove2, 2);
        });
        binding.remove3.setOnClickListener(v14 -> {
            clearImage(R.id.icon3, R.mipmap.ic_picture, R.id.remove3, 3);
        });
        binding.remove4.setOnClickListener(v14 -> {
            clearImage(R.id.icon4, R.mipmap.ic_picture, R.id.remove4, 4);
        });
        binding.remove5.setOnClickListener(v14 -> {
            clearImage(R.id.icon5, R.mipmap.ic_picture, R.id.remove5, 5);
        });
        binding.remove6.setOnClickListener(v14 -> {
            clearImage(R.id.icon6, R.mipmap.ic_picture, R.id.remove6, 6);
        });
        //Open price dialog
        binding.layoutInputPrice.setOnClickListener(v16 -> {
            final EditText input = new EditText(getContext());
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            new AlertDialog.Builder(getContext())
                    .setTitle(getString(R.string.price))
                    .setView(input)
                    .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            if(!input.getText().toString().trim().equals("")
                                    && !input.getText().toString().trim().isEmpty()) {
                                binding.priceValue.setText(input.getText().toString());
                                FinalPrice = Double.parseDouble(input.getText().toString());
                            }
                            else{
                                binding.priceValue.setText(getString(R.string.determination));
                                FinalPrice = -1;
                            }
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            binding.priceValue.setText(getString(R.string.determination));
                            FinalPrice = -1;
                        }
                    }).show();
        });
        //Check boxes
        binding.chbPhone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ShowPhone = isChecked;
        });
        binding.chbChat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            CanMessage = isChecked;
        });
        binding.chbStatus.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Status = isChecked;
        });
        //Location layout
        binding.layoutInputSpecifications.setOnClickListener(v1 -> {
            GoToLayout3From2(v);
        });

        binding.layoutInputCity.setOnClickListener(v1 -> {
            GoTOCityActivity();
        });
    }

    //Select image from gallery
    private void selectPicture(int index){
        pictureIndex = index;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickerActivityResultLauncher.launch(Intent.createChooser(intent, getString(R.string.image_selection)));
    }
    //Return result of Get_Content_Activity
    private final ActivityResultLauncher<Intent> pickerActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == RESULT_OK) {
            // This is the URI for the selected photo from the user.
            Uri photoUri = result.getData().getData();
            // Set URI to the image view to display the image.
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoUri);
                switch (pictureIndex){
                    case 1:
                        displayImageAndSaveInArray(bitmap, R.id.icon1,R.id.remove1, photoUri);
                        break;
                    case 2:
                        displayImageAndSaveInArray(bitmap, R.id.icon2,R.id.remove2, photoUri);
                        break;
                    case 3:
                        displayImageAndSaveInArray(bitmap, R.id.icon3,R.id.remove3, photoUri);
                        break;
                    case 4:
                        displayImageAndSaveInArray(bitmap, R.id.icon4,R.id.remove4, photoUri);
                        break;
                    case 5:
                        displayImageAndSaveInArray(bitmap, R.id.icon5,R.id.remove5, photoUri);
                        break;
                    case 6:
                        displayImageAndSaveInArray(bitmap, R.id.icon6,R.id.remove6, photoUri);
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    });
    //Display image and save
    private void displayImageAndSaveInArray(Bitmap bitmap, int displayViewId, int removeViewId, Uri photoUri){
        if(pictureIndex != -1) {
            ((ImageView) getView().findViewById(displayViewId)).setImageURI(photoUri);
            imgsInput[pictureIndex-1] = GetFileTemp(bitmap);
            getView().findViewById(removeViewId).setVisibility(View.VISIBLE);
        }
    }
    //Clear image input and array
    private void clearImage(int targetId,int iconId,int clearBtnId,int pictureIndex){
        ((ImageView)getView().findViewById(targetId)).setImageResource(iconId);
        (getView().findViewById(clearBtnId)).setVisibility(View.INVISIBLE);
        imgsInput[pictureIndex-1] = null;
    }

    @NonNull
    private File GetFileTemp(Bitmap bitmap){
        long leftLimit = 1L;
        long rightLimit = 2348327487326847682L;
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        File temp = new File(getContext().getCacheDir(), "img" + generatedLong + ".png");
        //Convert bitmap to byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos);
        byte[] bitmapdata = bos.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(temp);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Toast.makeText(getContext(), getString(R.string.there_problem_opening_file), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return temp;

    }
    private void createCollectionToFirestore(String title ,String description, String collectionName,String phone) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference dbAd = db.collection(collectionName);
        Map<String, Object> data = new HashMap<>();
        data.put("phone", phone);
        data.put("role", "admin");
        data.put("msg", "");
        data.put("time", new Date());
        dbAd.add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                SendAd(title, description, phone, collectionName);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Snackbar.make(binding.contentAd, "ثبت آگهی با مشکل مواجه شده است.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
    //Post ad to create endpoint
    private void PostAd(String title, String description){
        for (File f: imgsInput) {
            if(f != null) ContainImage = true;
        }
        String chatCollectionName = "null";
        String userPhone = SPreferences.getDefaults(Constants.Key_LoggedInUser_SP,getContext());

        progressDoalog = new ProgressDialog(getContext());
        progressDoalog.setMessage(getString(R.string.registering_an_ad));
        progressDoalog.setTitle(getString(R.string.upload_ad));
        progressDoalog.setCancelable(false);
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // show it
        if(CanMessage){
            Calendar cal = Calendar.getInstance();
            Date date = new Date();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            int month = cal.get(Calendar.MONTH);
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);

            chatCollectionName = year +","+ day +","+ month +","+ hours +","+ minute +","+ second +","+ userPhone;
            createCollectionToFirestore(title, description, chatCollectionName, userPhone);

        }else{
            SendAd(title, description, userPhone, "null");
        }
    }
    private void SendAd(String title, String description,String userPhone, String chatCollectionName){


        Map<String, RequestBody> data = new HashMap<>();
        data.put("Title",createPartFromString(title));
        data.put("ChatCollectionName",createPartFromString(chatCollectionName));
        data.put("Description",createPartFromString(description));
        data.put("Phone",createPartFromString(userPhone));
        endpoints.PostAd(data,
                FinalPrice == -1 ? null : FinalPrice,
                null,
                adCityId,
                getArguments().getInt(ARG_CATEGORY_ID),
                getArguments().getInt(ARG_SCATEGORY_ID),
                getArguments().getInt(ARG_SSCATEGORY_ID) == -1 ? null: getArguments().getInt(ARG_SSCATEGORY_ID),
                Status,
                ContainImage,
                CanMessage,
                ShowPhone,
                prepareFilePart("Image1", imgsInput[0]),
                prepareFilePart("Image2", imgsInput[1]),
                prepareFilePart("Image3", imgsInput[2]),
                prepareFilePart("Image4", imgsInput[3]),
                prepareFilePart("Image5", imgsInput[4]),
                prepareFilePart("Image6", imgsInput[5])
        ).enqueue(new Callback<DynamicResponseMdoel>() {

            @Override
            public void onResponse(Call<DynamicResponseMdoel> call, Response<DynamicResponseMdoel> response) {
                progressDoalog.dismiss();
                if(response.body() != null) {
                    if (response.body().getCode() != 400) {
                        try {
                            Snackbar.make(binding.contentAd, getString(R.string.ad_was_successfully_registered), Snackbar.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), getString(R.string.ad_was_successfully_registered), Toast.LENGTH_SHORT).show();
                        }
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                getActivity().onBackPressed();
                            }
                        }, 800);

                    }
                }
                else
                    try {
                        Snackbar.make(binding.contentAd, getString(R.string.ad_was_successfully_registered), Snackbar.LENGTH_SHORT).setAction(getString(R.string.try_again), v -> SendAd(title, description, userPhone,chatCollectionName)).show();
                    } catch (Exception e){
                Toast.makeText(getContext(), getString(R.string.there_is_a_problem_registering_the_ad), Toast.LENGTH_SHORT).show();
            }


            }

            @Override
            public void onFailure(Call<DynamicResponseMdoel> call, Throwable t) {
                progressDoalog.dismiss();
                Snackbar.make(binding.contentAd, getString(R.string.there_is_a_problem_registering_the_ad), Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.try_again), v -> SendAd(title, description, userPhone,chatCollectionName)).show();
            }
        });
    }

    //Prepare request parts
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName,File f){
        if(f != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse(getFileTypeByProbeContentType(f)), f);
            return MultipartBody.Part.createFormData(partName, f.getName(), requestFile);
        }return null;
    }
    @NonNull
    private RequestBody createPartFromString(String description){
        return  RequestBody.create(MultipartBody.FORM, description);
    }
    //Get file type by content
    public String getFileTypeByProbeContentType(File f){
        String fileType = "Undetermined";

        final File file = f;

        try{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                fileType = Files.probeContentType(file.toPath());
            }else{
             fileType = getMimeType(f.toURL().toString());
            }

        }

       catch (IOException ioException){


        }

        return fileType;

    }
    //Get file type by mime type
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
    //Layout controls
    private void GoToLayout1(View v){
        binding.layout1.setVisibility(View.VISIBLE);
        binding.btnCancel.setVisibility(View.VISIBLE);
        binding.layout2.setVisibility(View.GONE);
        binding.btnBack.setVisibility(View.INVISIBLE);
    }
    private void GoToLayout2(View v){
        binding.layout1.setVisibility(View.GONE);
        binding.btnCancel.setVisibility(View.INVISIBLE);
        binding.layout2.setVisibility(View.VISIBLE);
        binding.btnBack.setVisibility(View.VISIBLE);
        binding.adLocation.setText(adCityName);
    }
    private void GoToLayout3From2(View v){
        binding.layout2.setVisibility(View.GONE);
        binding.btnCancel.setVisibility(View.INVISIBLE);
        binding.layout3.setVisibility(View.VISIBLE);
        binding.btnBack2.setVisibility(View.VISIBLE);
        binding.cityNameTxt.setText(adCityName);
    }
    private void GoToLayout2from3(View v) {
        binding.layout2.setVisibility(View.VISIBLE);
        binding.btnBack.setVisibility(View.VISIBLE);
        binding.adLocation.setText(adCityName);
        binding.layout3.setVisibility(View.GONE);
        binding.btnBack2.setVisibility(View.INVISIBLE);
    }
    //Select new city
    private void GoTOCityActivity(){
        Intent go_to_select_city = new Intent(getContext(), CityActivity.class);
        go_to_select_city.putExtra(Constants.Source_Key, Constants.Source_Ad_City_Selection);
        startActivity(go_to_select_city);
    }


}