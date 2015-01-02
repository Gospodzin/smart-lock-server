package com.stak.smartlockserver.rest;

import android.util.Log;

import com.stak.smartlockserver.security.SecurityHelper;

import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import javax.inject.Inject;

/**
 * Created by gospo on 29.12.14.
 */
public class RegistrationResource extends ServerResource {

    @Inject
    SecurityHelper securityHelper;

    @Get
    @Post
    public String confirm() {
        Log.i(getClass().toString(), "Registration confirmation...");
        String username = getAttribute("username");
        String pin = getAttribute("pin");
        return securityHelper.confirmRegistration(username, pin);
    }
}
