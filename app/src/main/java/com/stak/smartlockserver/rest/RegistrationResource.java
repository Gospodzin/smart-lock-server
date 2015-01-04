package com.stak.smartlockserver.rest;

import android.util.Log;

import com.stak.smartlockserver.rest.dto.ConfirmDTO;
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

    @Post
    public String confirm(ConfirmDTO dto) {
        Log.i(getClass().toString(), "Registration confirmation...");
        String token = securityHelper.confirmRegistration(dto.getUsername(), dto.getPin());
        return  token;
    }
}
