package com.example.testapplication.data.db.models;

public class CityWeatherInfo {
    String cityName;
    String country;
    int currentTemperature;
    String currentDescription;

    public CityWeatherInfo(){

    }

    public CityWeatherInfo(String city_name, String _country, int current_temperature, String current_description){
        cityName = city_name;
        country = _country;
        currentTemperature = current_temperature;
        currentDescription = current_description;
    }

    public void setCityName(String city_name){
        cityName = city_name;
    }

    public String getCityName(){
        return cityName;
    }

    public void setCountry(String _country){
        country = _country;
    }

    public String getCountry(){
        return country;
    }

    public void setCurrentTemperature(int current_temperature){
        currentTemperature = current_temperature;
    }

    public int getCurrentTemperature(){
        return currentTemperature;
    }

    public void setCurrentDescription(String current_description){
        currentDescription = current_description;
    }

    public String getCurrentDescription(){
        return currentDescription;
    }
}
