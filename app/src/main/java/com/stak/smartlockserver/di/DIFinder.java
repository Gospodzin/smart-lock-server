package com.stak.smartlockserver.di;

import com.stak.smartlockserver.SmartLockApp;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.Finder;
import org.restlet.resource.ServerResource;

import java.util.logging.Level;

/**
 * Created by gospo on 29.12.14.
 */
public class DIFinder extends Finder {
    public DIFinder() {
        super();
    }

    public DIFinder(Context context) {
        super(context);
    }

    public DIFinder(Context context, Class<? extends ServerResource> targetClass) {
        super(context, targetClass);
    }

    @Override
    public ServerResource create(Class<? extends ServerResource> targetClass, Request request, Response response) {
        ServerResource result = null;

        if (targetClass != null) {
            try {
                result = targetClass.newInstance();
                SmartLockApp.inject(result);
            } catch (Exception e) {
                getLogger()
                        .log(Level.WARNING,
                                "Exception while instantiating the target server resource.",
                                e);
            }
        }

        return result;
    }
}
