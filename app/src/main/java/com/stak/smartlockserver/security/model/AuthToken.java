package com.stak.smartlockserver.security.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by gospo on 31.12.14.
 */
@DatabaseTable(tableName = "auth_tokens")
public class AuthToken {

    public static final String TOKEN = "token";
    public static final String USERNAME = "username";

    @DatabaseField(id=true, columnName = TOKEN)
    private String token;

    @DatabaseField(columnName = USERNAME)
    private String username;

    public AuthToken() {}

    public AuthToken(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
