package com.wifi.robot;

import android.app.Application;
import android.content.Context;

import com.qihoo360.replugin.RePlugin;

/**
 * Created by wong on 17-8-22.
 */

public class App extends Application{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        RePlugin.App.attachBaseContext(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RePlugin.App.onCreate();
    }
}
