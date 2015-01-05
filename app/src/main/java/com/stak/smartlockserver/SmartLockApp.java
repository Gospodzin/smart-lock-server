package com.stak.smartlockserver;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import dagger.ObjectGraph;

/**
 * Created by gospo on 29.12.14.
 */
public class SmartLockApp extends Application {

    private static ObjectGraph objectGraph;

    public static void inject(Object o) {
        objectGraph.inject(o);
    }

    @Override public void onCreate() {
        super.onCreate();
        objectGraph = ObjectGraph.create(getModules().toArray());
    }

    private List<Object> getModules() {
        return Arrays.<Object>asList(new SmartLockServerModule(this));
    }
}
