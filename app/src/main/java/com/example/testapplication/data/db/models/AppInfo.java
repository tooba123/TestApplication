package com.example.testapplication.data.db.models;

public class AppInfo {
    String appName;
    String packageName;

    public AppInfo(String app_name, String package_name){
        appName = app_name;
        packageName = package_name;
    }

    public void setAppName(String app_name){
        appName = app_name;
    }

    public String getAppName(){
        return appName;
    }

    public void setPackageName(String package_name){
        packageName = package_name;
    }

    public String getPackageName(){
        return packageName;
    }
}
