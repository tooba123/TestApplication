package com.example.testapplication.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testapplication.data.db.CityWeatherInfoDao;
import com.example.testapplication.data.network.NetworkManager;
import com.example.testapplication.data.network.network_response.CityWeatherInfoResponse;
import com.example.testapplication.data.network.network_response.Response;
import com.example.testapplication.data.service.SystemService;


public class CityWeatherRepository {

    private static NetworkManager networkManager;

    private static CityWeatherInfoDao cityWeatherInfoDao;

    MutableLiveData<Response> responseLiveData;

    Context context;


    public CityWeatherRepository(NetworkManager _networkManager, CityWeatherInfoDao _cityWeatherInfoDao, Context _context){
        networkManager = _networkManager;
        cityWeatherInfoDao = _cityWeatherInfoDao;
        context = _context;
    }


    public LiveData<Response> getCityWeatherInfo(String city_name){
        responseLiveData = new MutableLiveData<Response>();
        SystemService systemService = SystemService.getInstance(context);

        if(systemService.checkIfNetworkIsConnected()){

            //Run  Internet availablility check function on background thread
            systemService.isInternetAvailable(new SystemService.SystemServiceCallBack() {
                @Override
                public void internetCheckCallbackReceived(Object object) {
                    boolean internetAvailable = (boolean)object;
                    if(internetAvailable){
                        loadWeatherInfoFromNetwork(city_name);
                    }
                    else{
                        loadWeatherInfoFromDB(city_name);
                    }
                }
            });
        } else{
            loadWeatherInfoFromDB(city_name);
        }

        return responseLiveData;
    }

    private void loadWeatherInfoFromNetwork(String city_name){
        networkManager.getCityWeatherInfo(city_name, new NetworkManager.CityWeatherInfoCallBack() {
            @Override
            public void CityWeatherInfoResponseReceived(Response response) {
                responseLiveData.setValue(response);
            }
        });
    }

    private void loadWeatherInfoFromDB(String city_name){
        cityWeatherInfoDao.getCityWeatherResponseAsync(city_name, new CityWeatherInfoDao.DBCityWeatherInfoCallBack() {
            @Override
            public void CityWeatherResponseReceived(Response cityWeatherInfoResponse) {
                responseLiveData.setValue(cityWeatherInfoResponse);
            }
        });
    }




}
