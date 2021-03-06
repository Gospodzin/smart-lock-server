package com.stak.smartlockserver.view;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.j256.ormlite.dao.Dao;
import com.stak.smartlockserver.security.model.AuthToken;
import com.stak.smartlockserver.security.model.Registration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by gospo on 02.01.15.
 */
public class ViewHelper {
    @Inject
    Dao<Registration, String> registrationsDao;

    @Inject
    Dao<AuthToken, String> authTokensDao;

    public List<UserDTO> getUsersList() {
        try {
            final List<Registration> registrations = registrationsDao.queryForAll();
            List<AuthToken> authTokens = authTokensDao.queryForAll();

            List<UserDTO> users = new ArrayList<>();
            users.addAll(Collections2.transform(registrations, new Function<Registration, UserDTO>() {
                @Override
                public UserDTO apply(Registration registration) { return new UserDTO(registration.getUsername(), registration.getPin()); }
            }));
            users.addAll(Collections2.transform(authTokens, new Function<AuthToken, UserDTO>() {
                @Override
                public UserDTO apply(AuthToken authToken) { return new UserDTO(authToken.getUsername(), null); }
            }));

            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
