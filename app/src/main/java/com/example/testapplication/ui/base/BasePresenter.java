package com.example.testapplication.ui.base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;


public class BasePresenter implements LifecycleEventObserver {

    protected BaseView view;

    protected Context context;


    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

    }

    public void observeViewLifeCycle(){

    }

    public void onViewLoaded(){

    }


}
