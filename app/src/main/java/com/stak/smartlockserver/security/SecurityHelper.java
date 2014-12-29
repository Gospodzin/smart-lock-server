package com.stak.smartlockserver.security;

import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.KeyStore;
import java.sql.SQLException;
import java.util.UUID;

import javax.inject.Inject;

import static com.stak.smartlockserver.security.Constants.*;

/**
 * Created by gospo on 28.12.14.
 */
public class SecurityHelper {
    @Inject
    Dao<AuthToken, String> authTokenDao;

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

    public AuthToken register() {
        String token = UUID.randomUUID().toString();
        AuthToken authToken = new AuthToken(token);
        try {
            authTokenDao.create(authToken);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return authToken;
    }

    public boolean isAuthorized(AuthToken token) {
        boolean isAuthorized = false;

        try {
            isAuthorized = authTokenDao.idExists(token.getToken());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return  isAuthorized;
    }
}
