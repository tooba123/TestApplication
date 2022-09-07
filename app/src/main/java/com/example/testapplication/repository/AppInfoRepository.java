package com.example.testapplication.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.testapplication.data.db.models.AppInfo;
import com.example.testapplication.data.service.SystemService;
import java.util.ArrayList;

public class AppInfoRepository {

    private static SystemService systemService;

    public AppInfoRepository(Context context, SystemService _systemService){
        systemService = _systemService;
    }

    public LiveData<ArrayList<AppInfo>> getAppInfoList(){
        return systemService.getDeviceAppInfoList();
    }

}
