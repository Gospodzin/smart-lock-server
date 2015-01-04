package com.stak.smartlockserver.security;

import com.j256.ormlite.dao.Dao;
import com.stak.smartlockserver.security.model.AuthToken;
import com.stak.smartlockserver.security.model.Registration;

import org.restlet.security.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static com.stak.smartlockserver.security.Constants.*;

/**
 * Created by gospo on 28.12.14.
 */
public class SecurityHelper {
    @Inject
    Dao<Registration, String> registrationsDao;

    @Inject
    Dao<AuthToken, String> authTokensDao;

    @Inject
    KeyStoreGenerator ksg;

    private void generateKeyStore(OutputStream outputStream) {
        KeyStore keyStore = ksg.generateKeyStore();
        try {
            keyStore.store(outputStream, KEY_STORE_PASSWORD.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void generateKeyStoreFile(String path) throws IOException {
        FileOutputStream fos = new FileOutputStream(path);
        generateKeyStore(fos);
        fos.close();
    }

    public boolean isKeyStoreFileExistent(String path) {
        File file = new File(path);
        return file.exists();
    }

    public class UserExistsException extends Exception {public UserExistsException(){super("User already exists.");}}

    public Registration register(String userName) throws UserExistsException {
        try {
            if(!registrationsDao.queryForEq(Registration.USERNAME, userName).isEmpty()
                    || !authTokensDao.queryForEq(AuthToken.USERNAME, userName).isEmpty())
                throw new UserExistsException();

            SecureRandom secureRandom = new SecureRandom();
            String pin = new DecimalFormat("0000").format(Math.abs(secureRandom.nextInt(10000)));
            Registration registration = new Registration(userName, pin);

            registrationsDao.create(registration);
            return registration;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String confirmRegistration(String username, String pin) {
        try {
            Registration registration = new Registration(username, pin);
            if(registrationsDao.queryForMatching(registration).isEmpty())
                return null;

            registrationsDao.delete(registration);
            String token = UUID.randomUUID().toString();
            AuthToken authToken = new AuthToken(token, username);
            authTokensDao.create(authToken);
            return token;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String authenticate(String token) {
        try {
            List<AuthToken> authTokens = authTokensDao.queryForEq(AuthToken.TOKEN, token);
            return authTokens.isEmpty() ? null : authTokens.get(0).getUsername();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAuthorized(String token) {
        return authenticate(token) != null;
    }

    public void deleteUser(String username) {
        try {
            authTokensDao.delete(authTokensDao.queryForEq(AuthToken.USERNAME, username));
            registrationsDao.delete(registrationsDao.queryForEq(Registration.USERNAME, username));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
