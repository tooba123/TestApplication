package com.example.testapplication.ui.app_launcher;

import android.os.Bundle;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.testapplication.R;
import com.example.testapplication.adapters.AppListAdapter;
import com.example.testapplication.data.service.SystemService;
import com.example.testapplication.repository.AppInfoRepository;
import com.example.testapplication.ui.base.BaseActivity;
import com.example.testapplication.ui.base.BasePresenter;
import com.example.testapplication.data.db.models.AppInfo;
import java.util.ArrayList;

public class AppLauncherActivity extends BaseActivity implements ApplauncherView{

    RecyclerView rvListApps;
    ContentLoadingProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_app_launcher);

        initView();
        initListeners();
        setPresenter(new AppLauncherPresenter(context, this, new AppInfoRepository(context, SystemService.getInstance(context))));
        onViewLoaded();
    }

    @Override
    protected void setPresenter(BasePresenter _presenter){
        super.setPresenter(_presenter);

    }

    @Override
    protected void initView(){
        super.initView();
        rvListApps = findViewById(R.id.rv_list_apps);
        progressBar = findViewById(R.id.progress_bar);

    }

    @Override
    protected void initListeners(){
        super.initListeners();
    }

    @Override
    public void onViewLoaded(){
        super.onViewLoaded();
        presenter.onViewLoaded();
    }

    @Override
    public void observeLifeCycle(BasePresenter presenter) {
        getLifecycle().addObserver(presenter);
    }



    @Override
    public void displayAppInfoList(LiveData<ArrayList<AppInfo>> _appInfoListLiveData) {
        _appInfoListLiveData.observe(this, appInfoList -> {
            progressBar.hide();
            if(rvListApps.getAdapter() == null){
                rvListApps.setLayoutManager(new LinearLayoutManager(this));
                rvListApps.setAdapter(new AppListAdapter(appInfoList, context));

            } else {
                ArrayList<AppInfo> items = ((AppListAdapter)rvListApps.getAdapter()).getItems();
                items.clear();
                items.addAll(appInfoList);
                rvListApps.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void showProgressBar(){
        progressBar.show();
    }

    @Override
    public void hideProgressBar(){
        progressBar.hide();
    }
}
