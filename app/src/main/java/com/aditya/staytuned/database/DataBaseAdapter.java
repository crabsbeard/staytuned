package com.aditya.staytuned.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aditya.staytuned.pojo.PlaceDataObject;

import java.util.ArrayList;


/**
 * Created by devad_000 on 12-07-2015.
 * Database adapter and helper
 */


public class DataBaseAdapter {

    DataBaseHelper mDataBaseHelper;
    Context context;
    ArrayList<PlaceDataObject> placeDataObjectArrayList;

    public DataBaseAdapter(Context contex){
        this.context = contex;
        mDataBaseHelper = new DataBaseHelper(contex);
    }

    public boolean fetchUserData(String f_username, String f_password){
        SQLiteDatabase sqLiteDatabase = mDataBaseHelper.getWritableDatabase();
        boolean existsAndAuth = false;
        String[] usePass = new String[2];
        String[] columns = {mDataBaseHelper.UID,
                mDataBaseHelper.USERNAME_NAME,
                mDataBaseHelper.PASSWORD_NAME};

        Cursor cursor = sqLiteDatabase.query(mDataBaseHelper.USER_TABLE_NAME, columns, f_username, null, null, null, "1");

        int index1 = cursor.getColumnIndex(mDataBaseHelper.UID);
        int index2 = cursor.getColumnIndex(mDataBaseHelper.USERNAME_NAME);
        int index3 = cursor.getColumnIndex(mDataBaseHelper.PASSWORD_NAME);


        while(cursor.moveToNext()){
            usePass[1] = cursor.getString(index3);
            if(f_password==usePass[1]){
                existsAndAuth = true;
            }
        }
        return existsAndAuth;
    }

    public ArrayList<PlaceDataObject> fetchPlacesData(){
        placeDataObjectArrayList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = mDataBaseHelper.getWritableDatabase();
        String[] columns = {mDataBaseHelper.UID,
                mDataBaseHelper.USERNAME_NAME,
                mDataBaseHelper.PLACE_NAME,
                mDataBaseHelper.LATITUDE_NAME,
                mDataBaseHelper.LONGITUDE_NAME,
                mDataBaseHelper.LOCATION_NAME,
                mDataBaseHelper.TIMESTAMP_NAME};

        Cursor cursor = sqLiteDatabase.query(mDataBaseHelper.DATA_TABLE_NAME, columns, null, null, null, null, null);

        int index1 = cursor.getColumnIndex(mDataBaseHelper.UID);
        int index2 = cursor.getColumnIndex(mDataBaseHelper.USERNAME_NAME);
        int index3 = cursor.getColumnIndex(mDataBaseHelper.PLACE_NAME);
        int index4 = cursor.getColumnIndex(mDataBaseHelper.LATITUDE_NAME);
        int index5 = cursor.getColumnIndex(mDataBaseHelper.LONGITUDE_NAME);
        int index6 = cursor.getColumnIndex(mDataBaseHelper.LOCATION_NAME);
        int index7 = cursor.getColumnIndex(mDataBaseHelper.TIMESTAMP_NAME);

        while(cursor.moveToNext()){
            int uid = cursor.getInt(index1);
            String username = cursor.getString(index2);
            String placeName = cursor.getString(index3);
            double latitude = cursor.getDouble(index4);
            double longitude = cursor.getDouble(index5);
            String locationName = cursor.getString(index6);
            long timeStamp = cursor.getLong(index7);
            PlaceDataObject mPlaceDataObject = new PlaceDataObject(uid, username, placeName, latitude, longitude, locationName,  timeStamp);
            placeDataObjectArrayList.add(mPlaceDataObject);
        }
        return placeDataObjectArrayList;
    }

    public long insertPlacesData(String username, String place, double lat, double lon, String location, long timestamp ){
        SQLiteDatabase sqLiteDatabase = mDataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mDataBaseHelper.USERNAME_NAME, username);
        contentValues.put(mDataBaseHelper.PLACE_NAME, place);
        contentValues.put(mDataBaseHelper.LATITUDE_NAME, lat);
        contentValues.put(mDataBaseHelper.LONGITUDE_NAME, lon);
        contentValues.put(mDataBaseHelper.LOCATION_NAME, location);
        contentValues.put(mDataBaseHelper.TIMESTAMP_NAME, timestamp);
        long id = sqLiteDatabase.insert(mDataBaseHelper.DATA_TABLE_NAME, null, contentValues);
        return id;
    }

    public long insertUsersData(String username, String password){
        SQLiteDatabase sqLiteDatabase = mDataBaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(mDataBaseHelper.USERNAME_NAME, username);
        contentValues.put(mDataBaseHelper.PASSWORD_NAME, password);
        long id = sqLiteDatabase.insert(mDataBaseHelper.USER_TABLE_NAME, null, contentValues);
        return id;
    }


    class DataBaseHelper extends SQLiteOpenHelper{

        /**
         * static data for the database connections
         */

        private static final String DATABASE_NAME = "stayTunedDB_contest";
        private static final String DATA_TABLE_NAME = "information_table";
        private static final String USER_TABLE_NAME = "users_table";
        private static final String PLACE_NAME = "place";
        private static final String USERNAME_NAME = "username";
        private static final String LONGITUDE_NAME = "longitude";
        private static final String LATITUDE_NAME = "latitude";
        private static final String TIMESTAMP_NAME = "timeStamp";
        private static final String LOCATION_NAME = "location_name";
        private static final String PASSWORD_NAME = "password";
        private static final String EMPTY = " ";
        private static final String UID ="_id";
        private static final int DATABASE_VERSION = 1;
        Context context;

        /**
         * Build query to create table
         */
        private static final String createDataTableQuery = "CREATE TABLE"
                + EMPTY + DATA_TABLE_NAME + EMPTY
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EMPTY + USERNAME_NAME + EMPTY + "VARCHAR(100),"
                + EMPTY + PLACE_NAME + EMPTY + "TEXT,"
                + EMPTY + LATITUDE_NAME + EMPTY + "REAL,"
                + EMPTY + LONGITUDE_NAME + EMPTY + "REAL,"
                + EMPTY + LOCATION_NAME + EMPTY + "TEXT,"
                + EMPTY + TIMESTAMP_NAME + EMPTY + "INTEGER);";

        private static final String createUsersTableQuery = "CREATE TABLE"
                + EMPTY + USER_TABLE_NAME + EMPTY
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EMPTY + USERNAME_NAME + EMPTY + "VARCHAR(100) UNIQUE,"
                + EMPTY + PASSWORD_NAME + EMPTY + "VARCHAR(100));";

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
                db.execSQL(createUsersTableQuery);
                db.execSQL(createDataTableQuery);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
