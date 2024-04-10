package com.applications.divarapp.network;

import static com.applications.divarapp.BuildConfig.DEBUG;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class API {

    private static Retrofit retrofit;
    //private static String BASE_URL_API = "http://10.0.2.2:5087";
    //private static String BASE_URL_API = "http://172.16.1.2:5087";
    private static final String BASE_URL_API = "https://adsapi.mahicreativeteam.com";
    //private static final String BASE_URL_API = "http://...";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            if (DEBUG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS).build();

                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL_API).client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
            } else {

                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS).build();

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL_API).client(client)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }else return retrofit;
    }

    public static String getBaseUrlApi() {
        return BASE_URL_API;
    }
}

