package com.example.testapplication.data.db;

import android.content.ContentValues;
import android.content.Context;


import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Handler;
import android.os.Looper;
import com.example.testapplication.data.network.network_response.CityWeatherInfoResponse;
import com.example.testapplication.data.network.network_response.ErrorResponse;
import com.example.testapplication.data.network.network_response.Response;

import java.io.IOException;

public class CityWeatherInfoDao extends  DBHandler{


    public CityWeatherInfoDao(Context context) {
        super(context);
    }

    public void addNewCityWeather(String city_name, String country, int current_temperature, String current_description){
        SupportSQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CityWeatherTableSchema.ID, city_name);
        contentValues.put(CityWeatherTableSchema.CITY_NAME, city_name);
        contentValues.put(CityWeatherTableSchema.COUNTRY, country);
        contentValues.put(CityWeatherTableSchema.CURRENT_TEMPERATURE, current_temperature);
        contentValues.put(CityWeatherTableSchema.CURRENT_DESCRIPTION, current_description);

        db.insert(CityWeatherTableSchema.TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE , contentValues);
        try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Response getCityWeather(String city_name){
        SupportSQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                CityWeatherTableSchema.CITY_NAME,
                CityWeatherTableSchema.COUNTRY,
                CityWeatherTableSchema.CURRENT_TEMPERATURE,
                CityWeatherTableSchema.CURRENT_DESCRIPTION
        };
        SupportSQLiteQuery supportSQLiteQuery = SupportSQLiteQueryBuilder.builder(CityWeatherTableSchema.TABLE_NAME)
                .columns(projection)
                .selection(CityWeatherTableSchema.CITY_NAME + "= ?", new String[]{city_name})
                .create();


        Cursor cursor = db.query(supportSQLiteQuery);

        CityWeatherInfoResponse response = null;
        if(cursor!= null && cursor.moveToFirst()){
            response = new CityWeatherInfoResponse();
            response.setType(Response.Type.SUCCESS);
            int CITY_INDEX = cursor.getColumnIndex(CityWeatherTableSchema.CITY_NAME);
            response.setCity(cursor.getString(CITY_INDEX));

            int COUNTRY_INDEX = cursor.getColumnIndex(CityWeatherTableSchema.COUNTRY);
            response.setCountry(cursor.getString(COUNTRY_INDEX));

            int TEMPERATURE_INDEX = cursor.getColumnIndex(CityWeatherTableSchema.CURRENT_TEMPERATURE);
            response.setTemperature(cursor.getInt(TEMPERATURE_INDEX));

            int DESCRIPTION_INDEX = cursor.getColumnIndex(CityWeatherTableSchema.CURRENT_DESCRIPTION);
            response.setDescription(cursor.getString(DESCRIPTION_INDEX));
            cursor.close();
        }

        if(response == null){
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setType(Response.Type.ERROR);
            errorResponse.setMessage("Record not found");
            return errorResponse;
        }

        return response;
    }

    public void  getCityWeatherResponseAsync(String id, DBCityWeatherInfoCallBack dbCityWeatherListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Response response = getCityWeather(id);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dbCityWeatherListener.CityWeatherResponseReceived(response);
                    }
                });
            }
        }).start();
    }

    public interface DBCityWeatherInfoCallBack{
        void CityWeatherResponseReceived(Response cityWeatherInfoResponse);

    }

}
