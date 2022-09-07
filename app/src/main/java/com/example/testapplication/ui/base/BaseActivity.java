package com.example.testapplication.ui.base;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;

public class BaseActivity extends AppCompatActivity implements BaseView{

    protected BasePresenter presenter;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setPresenter(BasePresenter _presenter){
        presenter = _presenter;
    }
    protected void initView(){
        context = this;
    }

    protected void initListeners(){

    }

    @Override
    public void observeLifeCycle(BasePresenter presenter) {

    }

    @Override
    public void onViewLoaded() {

    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }
}