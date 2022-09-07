package com.example.testapplication.ui.base;

public interface BaseView {

    public void observeLifeCycle(BasePresenter presenter);

    public void onViewLoaded();

    public void showProgressBar();

    public void hideProgressBar();


}
