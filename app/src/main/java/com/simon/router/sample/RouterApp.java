package com.simon.router.sample;

import android.app.Application;

import com.simon.router.RouterInit;

/**
 * Created by sunmeng on 16/8/26.
 */
public class RouterApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RouterInit.init();
    }
}
