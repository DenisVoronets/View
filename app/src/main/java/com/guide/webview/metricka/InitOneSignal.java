package com.guide.webview.metricka;

import android.app.Application;

import com.onesignal.OneSignal;

/**
 * Инитим One Signal
 */
public class InitOneSignal extends Application {
    private static final String ONESIGNAL_APP_ID = "e4476c43-a6c5-4833-892c-fdb64d03a2be";


    @Override
    public void onCreate() {
        super.onCreate();
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
    }
}
