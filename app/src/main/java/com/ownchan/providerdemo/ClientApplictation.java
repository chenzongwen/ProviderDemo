package com.ownchan.providerdemo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Author: Owen Chan
 * DATE: 2017-02-24.
 */

public class ClientApplictation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
