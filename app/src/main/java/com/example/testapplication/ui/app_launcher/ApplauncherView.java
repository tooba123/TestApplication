package com.example.testapplication.ui.app_launcher;

import androidx.lifecycle.LiveData;

import com.example.testapplication.ui.base.BaseView;
import com.example.testapplication.data.db.models.AppInfo;
import java.util.ArrayList;

public interface ApplauncherView extends BaseView {


    public void displayAppInfoList(LiveData<ArrayList<AppInfo>> _appInfoList);
}
