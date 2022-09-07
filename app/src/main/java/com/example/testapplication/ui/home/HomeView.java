package com.example.testapplication.ui.home;

import androidx.lifecycle.LiveData;

import com.example.testapplication.data.db.models.CityWeatherInfo;
import com.example.testapplication.data.network.network_response.Response;
import com.example.testapplication.ui.base.BaseView;

public interface HomeView extends BaseView {

    public void showBatteryLevel(int level);

    public void setBatteryPercent(String text);

    public void moveToAppLauncherScreen();

    public void updateCityWeatherCard(LiveData<Response> networkResponseLiveData);

    public void setInfoOnCardView(CityWeatherInfo cityWeatherInfo);

    public void showErrorMessage(String message);


}
