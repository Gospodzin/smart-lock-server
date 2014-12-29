package com.stak.smartlockserver.rest;

import com.stak.smartlockserver.security.SecurityHelper;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Created by gospo on 29.12.14.
 */
public class RegistrationResource extends ServerResource {
    @Get
    public String register() {
        return new SecurityHelper().generateAuthToken();
    }
}
