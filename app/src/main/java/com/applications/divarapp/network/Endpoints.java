package com.applications.divarapp.network;

import com.applications.divarapp.models.AdBookMarkModelResponse;
import com.applications.divarapp.models.AdsModel;
import com.applications.divarapp.models.BookmarkDataModel;
import com.applications.divarapp.models.BookmarkModel;
import com.applications.divarapp.models.CategoryModel;
import com.applications.divarapp.models.ChatModelResponse;
import com.applications.divarapp.models.ChatResponse;
import com.applications.divarapp.models.ChatResponseCheck;
import com.applications.divarapp.models.CheckChatDataModel;
import com.applications.divarapp.models.CityModel;
import com.applications.divarapp.models.DynamicResponseMdoel;
import com.applications.divarapp.models.ProvinceModel;
import com.applications.divarapp.models.ReportDataModel;
import com.applications.divarapp.models.ResponseModel;
import com.applications.divarapp.models.SaveChatDataModel;
import com.applications.divarapp.models.SignupDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface Endpoints {

    //Get endpoints
    @GET("api/cities/get")
    Call<ArrayList<CityModel>> GetCities(@Header("SecretQHFyjbvfkj") String auth);
    @GET("api/categories/get")
    Call<ArrayList<CategoryModel>> GetCategories(@Header("SecretQGTYbvfkj") String auth);
    /* Temporary disable
    //@GET("api/cities/getp")
    //Call<ProvinceModel> GetProvince(@Query("cid") int cid);
     */
    @GET("api/ad/get")
    Call<ArrayList<AdsModel>> GetNewAds();
    @GET("api/ad/getad")
    Call<AdsModel> GetAd(@Query("aid") long aid);
    @GET("api/ad/mybookmarks")
    Call<ArrayList<AdBookMarkModelResponse>> GetMyBookMarks(@Query("ph") String ph);
    @GET("api/ad/bookcheck")
    Call<BookmarkModel> BookCheck(@Query("u") String u, @Query("aid") long aid);
    @GET("api/ad/filter/")
    Call<ArrayList<AdsModel>> FilterAds(@Query("s") String input, @Query("f") int f,@Query("s2") String input2);
    //Post endpoints
    @POST("api/Authenticate/vcode")
    Call<ResponseModel> PostVcode(@Header("SecretQHFydQTYC4") String auth, @Body String phone);
    @POST("api/Authenticate/Signup")
    Call<ResponseModel> PostSignup(@Header("Secret5oC3DgtCNC") String auth, @Body SignupDataModel model);
    @Multipart
    @POST("api/ad/create")
    Call<DynamicResponseMdoel> FileUpload(@Part MultipartBody.Part filePart);
    @POST("api/report/post")
    Call<DynamicResponseMdoel> Report(@Body ReportDataModel model);
    @POST("api/ad/bookmark")
    Call<DynamicResponseMdoel> AdBookmark(@Body BookmarkDataModel model);
    @POST("api/chat/save")
    Call<ChatResponse> SaveUserChat(@Body SaveChatDataModel model);
    @POST("api/chat/check")
    Call<ChatResponseCheck> CheckUserInChat(@Body CheckChatDataModel model);
    @POST("api/chat/mychat")
    Call<List<ChatModelResponse>> GetMyChats(@Query("phone") String phone);
    @Multipart
    @POST("api/ad/create")
    Call<DynamicResponseMdoel> PostAd(
            //Map parts for strings
            @PartMap Map<String , RequestBody> data,
            //Double parts
            @Part("FinalPrice") Double finalPrice,
            //Integer parts
            @Part("AreaId") Integer AreaId,
            @Part("CityId") Integer CityId,
            @Part("CategoryId") int CategoryId,
            @Part("SubCategoryId") int SubCategoryId,
            @Part("SubSubCategoryId") Integer SubSubCategoryId,
            //Boolean parts
            @Part("Status") boolean Status,
            @Part("ContainImage") boolean ContainImage,
            @Part("CanMessage") boolean CanMessage,
            @Part("ShowPhone") boolean ShowPhone,
            //File parts
            @Part MultipartBody.Part Image1,
            @Part MultipartBody.Part Image2,
            @Part MultipartBody.Part Image3,
            @Part MultipartBody.Part Image4,
            @Part MultipartBody.Part Image5,
            @Part MultipartBody.Part Image6
            );
}
