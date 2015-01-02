package com.stak.smartlockserver.rest;

import android.util.Log;

import com.stak.smartlockserver.LockManager;
import com.stak.smartlockserver.security.SecurityHelper;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import javax.inject.Inject;

/**
 * Created by gospo on 29.12.14.
 */
public class SmartLockResource extends ServerResource {
    @Inject
    SecurityHelper securityHelper;

    @Inject
    LockManager lockManager;

    @Get
    @Post
    public boolean command() {
        String token = getAttribute("token");
        // if not authorized return false
        if(!securityHelper.isAuthorized(token))
            return false;

        // if authorized
        String command = getAttribute("command");
        Log.i(getClass().toString(), "Command " + command + "...");
        switch(command.toUpperCase()) {
            case "OPEN":
                lockManager.open();
                break;
            case "CLOSE":
                lockManager.close();
                break;
            default:
                // no such command
                return false;
        }
        return true;
    }
}
