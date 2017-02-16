package com.ownchan.providerdemo;

import android.database.ContentObserver;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Author: Owen Chan
 * DATE: 2017-02-15.
 */

public class PersonObserver extends ContentObserver {

    public static final String TAG = "PersonObserver";
    private Handler handler;

    public PersonObserver(Handler handler) {
        super(handler);
        this.handler = handler;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.i(TAG, "data changed , try to require.");
        Message msg = Message.obtain();
        handler.sendMessage(msg);
    }
}
