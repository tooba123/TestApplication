package com.example.testapplication.data.network.network_response;

public class CityWeatherInfoResponse extends Response {

    private String city;
    private String country;
    private int temperature;
    private String description;

    public void setCountry(String _country){
        country = _country;
    }

    public String getCountry() {
        return country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTemperature(int _temperature){
        temperature = _temperature;
    }

    public int getTemperature(){
        return temperature;
    }
}
