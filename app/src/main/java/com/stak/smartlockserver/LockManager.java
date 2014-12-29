package com.stak.smartlockserver;

import android.util.Log;

/**
 * Created by gospo on 29.12.14.
 */
public class LockManager {
    public void open(){
        Log.i(getClass().toString(), "Opening lock...");
    }

    public void close(){
        Log.i(getClass().toString(), "Closing lock...");
    }
}
