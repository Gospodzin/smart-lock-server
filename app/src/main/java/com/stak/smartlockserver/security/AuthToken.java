package com.stak.smartlockserver.security;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by gospo on 29.12.14.
 */

@DatabaseTable(tableName = "auth_tokens")
public class AuthToken {
    @DatabaseField(id=true)
    private String token;

    public AuthToken() {}

    public AuthToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
