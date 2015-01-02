package com.stak.smartlockserver.view;

/**
 * Created by gospo on 02.01.15.
 */
public class UserDTO {
    private String username;
    private String pin;

    public UserDTO(String username, String pin) {
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

    @Override
    public String toString() {
        return username + (pin != null ? " " + pin : "");
    }
}
