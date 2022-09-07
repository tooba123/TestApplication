package com.example.testapplication.data.network;


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.testapplication.data.network.network_response.CityWeatherInfoResponse;
import com.example.testapplication.data.network.network_response.Response;

public class NetworkManager {

    private WeatherAPI weatherAPI;
    private Context context;

    private  static  NetworkManager networkManager;

    private NetworkManager(WeatherAPI _weatherAPI){
        weatherAPI = _weatherAPI;
    }

    public static NetworkManager getInstance(Context context){
        if(networkManager == null){
            networkManager = new NetworkManager(new WeatherAPI(context));
        }
        return networkManager;
    }

    public void getCityWeatherInfo(String city_name, CityWeatherInfoCallBack cityWeatherInfoCallBack){

        weatherAPI.getWeatherInfo(city_name, new WeatherAPI.weatherAPIResponseCallBack() {
            @Override
            public void WeatherAPIResponseReceieved(Response response) {
                cityWeatherInfoCallBack.CityWeatherInfoResponseReceived(response);
            }
        });
    }

    public interface CityWeatherInfoCallBack{
       public void CityWeatherInfoResponseReceived(Response response);
    }



}
