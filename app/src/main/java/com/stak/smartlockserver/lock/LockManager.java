package com.stak.smartlockserver.lock;

import android.util.Log;

import com.stak.smartlockserver.lock.drivers.EngineDriver;

import javax.inject.Inject;

/**
 * Created by gospo on 29.12.14.
 */
public class LockManager {

    public static final int TIME = 5000;

    @Inject
    EngineDriver engineDriver;

    public void open(){
        Log.i(getClass().toString(), "Opening lock...");
        engineDriver.spinForward(TIME);
    }

    public void close(){
        Log.i(getClass().toString(), "Closing lock...");
        engineDriver.spinBackward(TIME);
    }
}
