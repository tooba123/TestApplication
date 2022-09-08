package com.example.testapplication.data.db;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;

public  class DBHandler {

   private  SupportSQLiteOpenHelper supportSQLiteOpenHelper;
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
      supportSQLiteOpenHelper = createSqliteOpenHelper(context, DB_NAME, DB_VERSION);
   }


   public SupportSQLiteOpenHelper createSqliteOpenHelper(Context context, String dbName, int version){

      SupportSQLiteOpenHelper.Configuration configuration = createConfiguration(context, dbName, version);
      return new FrameworkSQLiteOpenHelperFactory().create(configuration) ;
   }

   public SupportSQLiteDatabase getWritableDatabase(){
      return supportSQLiteOpenHelper.getWritableDatabase();
   }

   public SupportSQLiteDatabase getReadableDatabase(){
      return supportSQLiteOpenHelper.getReadableDatabase();
   }



   private SupportSQLiteOpenHelper.Configuration createConfiguration(Context context, String dbName, int version){
      return SupportSQLiteOpenHelper
              .Configuration
              .builder(context)
              .name(dbName)
              .callback(new SupportSQLiteOpenHelper.Callback(version) {
                 @Override
                 public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    String query = "CREATE TABLE " + CityWeatherTableSchema.TABLE_NAME + "("
                            + CityWeatherTableSchema.ID + " TEXT PRIMARY KEY, "
                            + CityWeatherTableSchema.CITY_NAME + " TEXT, "
                            + CityWeatherTableSchema.COUNTRY + " TEXT, "
                            + CityWeatherTableSchema.CURRENT_TEMPERATURE + " INTEGER, "
                            + CityWeatherTableSchema.CURRENT_DESCRIPTION + " TEXT) ";

                    db.execSQL(query);
                 }

                 @Override
                 public void onUpgrade(@NonNull SupportSQLiteDatabase db, int oldVersion, int newVersion) {

                 }
              })
              .build();
   }

}
