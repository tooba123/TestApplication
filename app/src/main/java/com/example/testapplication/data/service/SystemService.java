package com.example.testapplication.data.service;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.testapplication.data.db.models.AppInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class SystemService {

    Context context;
    private static SystemService systemService;
    private SystemService(Context _context){
        context = _context;
    }

    private SystemService(){

    }

    public static SystemService getInstance(Context _context){
        if(systemService == null){
            systemService = new SystemService(_context);
        }
        return systemService;
    }

    private LiveData<ArrayList<AppInfo>> loadDeviceAppInfoList() {
        MutableLiveData<ArrayList<AppInfo>> appInfoLiveData = new MutableLiveData<ArrayList<AppInfo>>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<AppInfo>  appInfoList = performLoadingAppInfoList();
                Handler mainThreadHandler = new Handler(Looper.getMainLooper());
                mainThreadHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        appInfoLiveData.setValue(appInfoList);
                    }
                });
            }
        }).start();

        return appInfoLiveData;
    }

    private ArrayList<AppInfo> performLoadingAppInfoList(){
        ArrayList<AppInfo> appInfoArrayList = new ArrayList<AppInfo>();

        PackageManager pm = context.getPackageManager();
        List<PackageInfo> appInfoList = pm.getInstalledPackages(0);
        for(PackageInfo packageInfo : appInfoList){

            //if system app is selected then skip
            if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
                continue;
            }

            if(packageInfo.versionName == null){
                continue;
            }
            String appName = packageInfo.applicationInfo.loadLabel(pm).toString();
            String packageName = packageInfo.packageName;
            AppInfo appInfo = new AppInfo(appName, packageName);
            appInfoArrayList.add(appInfo);
        }

        return appInfoArrayList;
    }


    public LiveData<ArrayList<AppInfo>> getDeviceAppInfoList(){
        return loadDeviceAppInfoList();
    }

    public boolean checkIfNetworkIsConnected(){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        return ((cm.getActiveNetworkInfo() != null) && cm.getActiveNetworkInfo().isConnected());
    }

    public void isInternetAvailable(SystemServiceCallBack systemServiceCallBack){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress ipaddress = InetAddress.getByName("google.com");
                    systemServiceCallBack.internetCheckCallbackReceived(!ipaddress.equals(""));

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    systemServiceCallBack.internetCheckCallbackReceived(false);
                }
            }
        }).start();

    }

    public interface SystemServiceCallBack{
        public void internetCheckCallbackReceived(Object internetAvailable);
    }
}
