package com.applications.divarapp.helpers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "applicationdb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "RecentlyViewed";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String AD_ID_COL = "AdId";

    // below variable id for our course duration column.
    private static final String AD_Title_COL = "AdTitle";
    public DBHelper(Context context) {
        super(context, DB_NAME , null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + AD_ID_COL + " VARCHAR,"
                + AD_Title_COL + " VARCHAR)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addRecentlyView(String adId, String adTitle) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(AD_ID_COL, adId);
        values.put(AD_Title_COL, adTitle);


        db.insert(TABLE_NAME, null, values);

        db.close();
    }
    public boolean isExistAd(String adId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+AD_ID_COL+" FROM " + TABLE_NAME + " WHERE " + AD_ID_COL + " = '"+adId+"'",null);
        return cursor.getCount() != 0;
    }


}
