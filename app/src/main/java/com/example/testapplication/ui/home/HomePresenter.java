package com.example.testapplication.ui.home;

import static android.content.Intent.ACTION_BATTERY_CHANGED;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;

import com.example.testapplication.data.network.network_response.Response;
import com.example.testapplication.repository.CityWeatherRepository;
import com.example.testapplication.ui.base.BasePresenter;

public class HomePresenter extends BasePresenter implements LifecycleEventObserver {

    BroadcastReceiver batteryLevelReceiver;

    int BATTERY_LEVEL_MAX_HEIGHT_IN_DP = 100;

    CityWeatherRepository cityWeatherRepository;

    private int selectedCardIndex = 0;
    private int lastCardIndex = 5;

    private String[] cityList =  {"Beijing" , "Berlin", "Cardiff", "Edinburgh", "London", "Nottingham"};

    HomePresenter(Context context, HomeView view, CityWeatherRepository _cityWeatherRepository){
        this.view = view;
        this.context = context;
        cityWeatherRepository = _cityWeatherRepository;

        batteryLevelReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateBatteryData(intent);
            }
        };
    }

    @Override
    public void onViewLoaded(){
       observeViewLifeCycle();
       updateCityWeatherCard();
    }


    private int convertDPtoPx(Context context, int dp){
        float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int)( dp * scale + 0.5f);
        return pixels;
    }



    private void updateBatteryData(Intent intent){

        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int dp = getUpdatedBatteryLevel(level);
        int pixels = convertDPtoPx(context, dp);

        ((HomeView)view).showBatteryLevel(pixels);
        String batteryLevelText = level + "%";
        ((HomeView)view).setBatteryPercent(batteryLevelText);
    }

    public int getUpdatedBatteryLevel(int level){
        return (BATTERY_LEVEL_MAX_HEIGHT_IN_DP * level) / 100;
    }



    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event){
            case ON_RESUME:
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(ACTION_BATTERY_CHANGED);
                context.registerReceiver(batteryLevelReceiver, intentFilter);
                break;

            case ON_PAUSE:
                context.unregisterReceiver(batteryLevelReceiver);
                break;
        }
    }

    @Override
    public void observeViewLifeCycle(){
        super.observeViewLifeCycle();
        ((HomeView)view).observeLifeCycle(this);
    }

    public void AppLauncherButtonClicked(){
        ((HomeView)view).moveToAppLauncherScreen();
    }

    public void cityWeatherCardButtonClicked(){
        if(selectedCardIndex == lastCardIndex){
            selectedCardIndex = 0;
        } else{
            selectedCardIndex = selectedCardIndex + 1;
        }
        updateCityWeatherCard();
    }

    private void updateCityWeatherCard(){
        LiveData<Response> cityWeatherResponseLiveData = cityWeatherRepository.getCityWeatherInfo(cityList[selectedCardIndex]);
        ((HomeView)view).updateCityWeatherCard(cityWeatherResponseLiveData);
    }



}
