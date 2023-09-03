package com.applications.divarapp.utils;

public class Constants {

    //Shared preferences keys
    public static final String Key_City_SP = "City";
    public static final String Key_LoggedInUser_SP = "Login";
    public static final String Key_LoggedInUserCode_SP = "LoginCode";
    public static final String Key_LoggedInUserToken_SP = "Token";
    //Secret hashed for cities endpoint
    public static final String Secret_Cities = "823C05D3E2A9D4E4698B2A2D047338AF";
    // Secret hashed for categories endpoint
    public static final String Secret_Categories = "34D39E2890255FA8936AD563489FAEBB";
    //Fragment tags

    //Lock
    public static final String Lock_FragmentChat_Tag = "LOCK_CHAT_PAGE";
    public static final String Home_Fragment_Tag = "HOME_PAGE";
    //Category
    public static final String Category_Fragment_Tag = "CATEGORY_PAGE";
    public static final String SubCategory_Fragment_Tag = "SUBCATEGORY_PAGE";
    public static final String SubCategory_Search_Fragment_Tag = "SUBCATEGORY_SEARCH_PAGE";
    //AD
    public static final String ADCategory_Fragment_Tag = "ADCATEGORY_PAGE";
    public static final String ADSubCategory_Fragment_Tag = "ADSUBCATEGORY_PAGE";
    public static final String AD_Fragment_Tag = "AD_PAGE";
    //Note
    public static final String NOTE_Fragment_Tag = "NOTE_PAGE";
    //Search
    public static final String Search_Fragment_Tag = "SEARCH_PAGE";
    //Chat fragment
    public static final String Chat_Fragment_Tag = "CHAT_PAGE";
    //Setting fragment
    public static final String Setting_Fragment_Tag = "SETTING_PAGE";
    //MyAds fragment
    public static final String My_Ads_Fragment_Tag = "MY_ADS_PAGE";
    //MyBookmarks
    public static final String My_Ad_Bookmarks_Fragment_Tag = "MY_Bookmarks_PAGE";
    //Report
    public static final String Report_Fragment_Tag = "REPORT_PAGE";
    //Enums
    public enum page_state {CATEGORIES, AD, SEARCH}
    public enum page_search_state {CATEGORIY, TEXT}
    //Source of calling activites
    public static final String Source_Key = "source";
    public static final String Source_First_City_Selection = "first";
    public static final String Source_Home_City_Selection = "home";
    public static final String Source_Ad_City_Selection = "ad";
    //Filters code
    public static final int FilterByName = 1;
    public static final int FilterByCity = 2;
    public static final int FilterByCategory = 3;
    public static final int FilterBySubCategory = 4;
    public static final int FilterBySubSubCategory = 5;
    public static final int FilterByCategoryWithName = 6;
    public static final int FilterByUser = 7;
    public static final int FilterByOwnerAd = 8;
    //Show Ad
    public static final String Key_Extra_AId = "AID";



}
