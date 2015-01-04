package com.stak.smartlockserver.rest;

import android.util.Log;

import com.stak.smartlockserver.lock.LockManager;
import com.stak.smartlockserver.rest.dto.CommandDTO;
import com.stak.smartlockserver.security.SecurityHelper;

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

    @Post
    public boolean command(CommandDTO dto) {

        // if not authorized return false
        if(!securityHelper.isAuthorized(dto.getToken()))
            return false;

        // if authorized
        Log.i(getClass().toString(), "Command " + dto.getCommand() + "...");
        switch(dto.getCommand().toUpperCase()) {
            case "OPEN":
                lockManager.open();
                return true;
            case "CLOSE":
                lockManager.close();
                return true;
            default:
                // no such command
                return false;
        }
    }
}
