package com.example.testapplication.data.network;


import static com.example.testapplication.data.network.WeatherAPI.Constants.URL_BEIJING_WEATHER;
import static com.example.testapplication.data.network.WeatherAPI.Constants.URL_BERLIN_WEATHER;
import static com.example.testapplication.data.network.WeatherAPI.Constants.URL_CARDIFF_WEATHER;
import static com.example.testapplication.data.network.WeatherAPI.Constants.URL_EDINBURG_WEATHER;
import static com.example.testapplication.data.network.WeatherAPI.Constants.URL_LONDON_WEATHER;
import static com.example.testapplication.data.network.WeatherAPI.Constants.URL_NOTTINGHAM_WEATHER;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.JsonReader;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testapplication.data.db.CityWeatherInfoDao;
import com.example.testapplication.data.network.network_response.CityWeatherInfoResponse;
import com.example.testapplication.data.network.network_response.ErrorResponse;
import com.example.testapplication.data.network.network_response.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;


public class WeatherAPI {

    private Context context;
    protected static class Constants{
        public  static String URL_BEIJING_WEATHER       = "https://weather.bfsah.com/beijing";
        public static  String URL_BERLIN_WEATHER        = "https://weather.bfsah.com/berlin";
        public static  String URL_CARDIFF_WEATHER       = "https://weather.bfsah.com/cardiff";
        public static  String URL_EDINBURG_WEATHER      = "https://weather.bfsah.com/edinburgh";
        public static  String URL_LONDON_WEATHER        = "https://weather.bfsah.com/london";
        public static  String URL_NOTTINGHAM_WEATHER    = "https://weather.bfsah.com/nottingham";
    }



    public WeatherAPI(Context _context){
        context = _context;
    }

    public void getWeatherInfo(String option, weatherAPIResponseCallBack weatherAPIResponseCallBack){
        LiveData<Response> networkResponseLiveData = null;
        switch (option){
            case "Beijing" :
                performLoadingWeatherData(URL_BEIJING_WEATHER, weatherAPIResponseCallBack);
                break;

            case "Berlin":
                performLoadingWeatherData(URL_BERLIN_WEATHER, weatherAPIResponseCallBack);
                break;

            case "Cardiff":
                performLoadingWeatherData(URL_CARDIFF_WEATHER, weatherAPIResponseCallBack);
                break;

            case "Edinburgh":
                performLoadingWeatherData(URL_EDINBURG_WEATHER, weatherAPIResponseCallBack);
                break;

            case "London":
                performLoadingWeatherData(URL_LONDON_WEATHER, weatherAPIResponseCallBack);
                break;

            case "Nottingham":
                performLoadingWeatherData(URL_NOTTINGHAM_WEATHER, weatherAPIResponseCallBack);
                break;
        }
    }

    private void performLoadingWeatherData(String url_string, weatherAPIResponseCallBack weatherAPIResponseCallBack){
        MutableLiveData<Response> networkResponseLiveData = new MutableLiveData<Response>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                try {
                    URL url = new URL(url_string);
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");

                    int responseCode = httpURLConnection.getResponseCode();


                    if(responseCode == HttpURLConnection.HTTP_OK){
                        CityWeatherInfoResponse cityWeatherInfoResponse = parseSuccessResponse(httpURLConnection.getInputStream());
                        CityWeatherInfoDao dao = new CityWeatherInfoDao(context);
                        dao.addNewCityWeather(cityWeatherInfoResponse.getCity(), cityWeatherInfoResponse.getCountry(), cityWeatherInfoResponse.getTemperature(), cityWeatherInfoResponse.getDescription());
                        sendResponse(cityWeatherInfoResponse, weatherAPIResponseCallBack);
                    }

                    else{
                        sendErrorResponse(httpURLConnection.getErrorStream(), weatherAPIResponseCallBack);
                    }




                } catch (IOException e) {
                    sendErrorResponse(httpURLConnection.getErrorStream(), weatherAPIResponseCallBack);
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public CityWeatherInfoResponse parseSuccessResponse(InputStream inputStream){
        try {
            CityWeatherInfoResponse cityWeatherInfoResponse = new CityWeatherInfoResponse();
            cityWeatherInfoResponse.setType(Response.Type.SUCCESS);
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            jsonReader.beginObject();
            while(jsonReader.hasNext()){
               String name =  jsonReader.nextName();
                switch (name){
                    case "city":
                        cityWeatherInfoResponse.setCity(jsonReader.nextString());
                        break;

                    case "country":
                        cityWeatherInfoResponse.setCountry(jsonReader.nextString());
                        break;

                    case "temperature":
                        cityWeatherInfoResponse.setTemperature(jsonReader.nextInt());
                        break;

                    case "description":
                        cityWeatherInfoResponse.setDescription(jsonReader.nextString());
                        break;
                }
            }
            jsonReader.endObject();

            return cityWeatherInfoResponse;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

       return null;
    }

    public ErrorResponse parseErrorResponse(InputStream inputStream){
        try {
            ErrorResponse errorResponse = null;
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            jsonReader.beginObject();
            while(jsonReader.hasNext()){
                String name =  jsonReader.nextName();
                switch (name){
                    case "error":
                        errorResponse = readErrorObject(jsonReader);
                        break;
                }
            }
            jsonReader.endObject();

            return errorResponse;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ErrorResponse readErrorObject(JsonReader jsonReader){
        try {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setType(Response.Type.ERROR);
            jsonReader.beginObject();
            while (jsonReader.hasNext()){
                String name = jsonReader.nextName();
                switch (name){
                    case "code" :
                        errorResponse.setCode(jsonReader.nextInt());
                        break;

                    case "message" :
                        errorResponse.setMessage(jsonReader.nextString());
                        break;
                }
            }
            jsonReader.endObject();
            return errorResponse;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sendResponse(Response response, weatherAPIResponseCallBack weatherAPIResponseCallBack){
       Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                weatherAPIResponseCallBack.WeatherAPIResponseReceieved(response);

            }
        });
    }

    public void sendErrorResponse(InputStream inputStream, weatherAPIResponseCallBack weatherAPIResponseCallBack){
        Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        mainThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                weatherAPIResponseCallBack.WeatherAPIResponseReceieved(parseErrorResponse(inputStream));
                //networkResponseLiveData.setValue(parseErrorResponse(inputStream));
            }
        });
    }

    public void printErrorMessage(String message){
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public interface weatherAPIResponseCallBack{
        public void WeatherAPIResponseReceieved(Response response);
    }


}
