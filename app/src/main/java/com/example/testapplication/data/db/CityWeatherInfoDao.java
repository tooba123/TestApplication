package com.example.testapplication.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;

import com.example.testapplication.data.network.network_response.CityWeatherInfoResponse;
import com.example.testapplication.data.network.network_response.ErrorResponse;
import com.example.testapplication.data.network.network_response.Response;

public class CityWeatherInfoDao extends  DBHandler{


    public CityWeatherInfoDao(Context context) {
        super(context);
    }

    public void addNewCityWeather(String city_name, String country, int current_temperature, String current_description){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(CityWeatherTableSchema.ID, city_name);
        contentValues.put(CityWeatherTableSchema.CITY_NAME, city_name);
        contentValues.put(CityWeatherTableSchema.COUNTRY, country);
        contentValues.put(CityWeatherTableSchema.CURRENT_TEMPERATURE, current_temperature);
        contentValues.put(CityWeatherTableSchema.CURRENT_DESCRIPTION, current_description);

        db.insertWithOnConflict(CityWeatherTableSchema.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);


        String query = "INSERT INTO " +  CityWeatherTableSchema.TABLE_NAME+"(" + CityWeatherTableSchema.ID +"," + CityWeatherTableSchema.CITY_NAME + "," + CityWeatherTableSchema.COUNTRY + "," + CityWeatherTableSchema.CURRENT_TEMPERATURE +","+ CityWeatherTableSchema.CURRENT_DESCRIPTION+") values(" + "'" + city_name + "'" +", '" +city_name + "'" +", '" + country + "' " + ", " + current_temperature + ""+ ", '"+current_description + "');";
        /*if(getCityWeather(city_name) == null){
            db.execSQL(query);
        }*/
        db.close();

    }

    public Response getCityWeather(String city_name){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + CityWeatherTableSchema.TABLE_NAME + " WHERE " + CityWeatherTableSchema.CITY_NAME + " = '"+ city_name + "'", null);
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
            errorResponse.setMessage("Not Found");
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
