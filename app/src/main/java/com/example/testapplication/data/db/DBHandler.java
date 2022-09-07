package com.example.testapplication.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

 public abstract class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME =  "test_db";
    private static final int    DB_VERSION = 1;

    protected static class CityWeatherTableSchema {
       public static String TABLE_NAME = "city_weather_info";
       public static String ID = "id";
       public static String CITY_NAME = "city_name";
       public static String COUNTRY = "country";
       public static String CURRENT_TEMPERATURE = "current_temperature";
       public static String CURRENT_DESCRIPTION = "current_description";
    }


    public DBHandler(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       // Create all tables here
      String query = "CREATE TABLE " + CityWeatherTableSchema.TABLE_NAME + "("
              + CityWeatherTableSchema.ID + " TEXT PRIMARY KEY, "
              + CityWeatherTableSchema.CITY_NAME + " TEXT, "
              + CityWeatherTableSchema.COUNTRY + " TEXT, "
              + CityWeatherTableSchema.CURRENT_TEMPERATURE + " INTEGER, "
              + CityWeatherTableSchema.CURRENT_DESCRIPTION + " TEXT) ";

       sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
