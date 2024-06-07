package com.example.skyTestLauncher;

import android.app.Application;


public class SkyTestLauncherApplication extends Application {
    private static SkyTestLauncherApplication context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;


    }

    //全局获取上下文方法
    public static SkyTestLauncherApplication getContext() {
        if (context == null) {
            context = new SkyTestLauncherApplication();
        }
        return context;
    }
}
