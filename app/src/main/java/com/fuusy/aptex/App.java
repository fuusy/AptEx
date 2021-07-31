package com.fuusy.aptex;

import android.app.Application;

import com.fuusy.arouterapi.ARouter;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.getInstance().init(this);
    }
}
