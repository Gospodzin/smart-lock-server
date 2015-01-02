package com.stak.smartlockserver.security.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by gospo on 01.01.15.
 */
@DatabaseTable(tableName = "registrations")
public class Registration {

    public static final String USERNAME = "username";
    public static final String PIN = "pin";

    @DatabaseField(id=true, columnName = USERNAME)
    private String username;

    @DatabaseField(columnName = PIN)
    private String pin;

    public Registration() {}

    public Registration(String username, String pin) {
        this.username = username;
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
