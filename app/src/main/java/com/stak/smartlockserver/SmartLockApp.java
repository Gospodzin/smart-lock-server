package com.stak.smartlockserver;

import android.app.Application;

import com.stak.smartlockserver.security.SecurityHelper;

import org.restlet.engine.Engine;
import org.restlet.ext.gson.GsonConverter;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

/**
 * Created by gospo on 29.12.14.
 */
public class SmartLockApp extends Application {

    @Inject
    SecurityHelper securityHelper;

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
