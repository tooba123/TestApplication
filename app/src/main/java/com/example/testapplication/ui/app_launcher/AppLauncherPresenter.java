package com.example.testapplication.ui.app_launcher;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.testapplication.repository.AppInfoRepository;
import com.example.testapplication.ui.base.BasePresenter;
import com.example.testapplication.data.db.models.AppInfo;
import java.util.ArrayList;


public class AppLauncherPresenter extends BasePresenter {

    AppInfoRepository appInfoRepository;

    AppLauncherPresenter(Context _context, ApplauncherView _applauncherView, AppInfoRepository _appInfoRepository){
        context = _context;
        view = _applauncherView;
        appInfoRepository = _appInfoRepository;
    }

    @Override
    public void onViewLoaded(){
        view.showProgressBar();
        LiveData<ArrayList<AppInfo>> appInfoList = appInfoRepository.getAppInfoList();
        ((ApplauncherView)view).displayAppInfoList(appInfoList);

    }


}
