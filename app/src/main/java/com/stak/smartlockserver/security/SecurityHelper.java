package com.stak.smartlockserver.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import static com.stak.smartlockserver.security.Constants.*;

/**
 * Created by gospo on 28.12.14.
 */
public class SecurityHelper {
    KeyStoreGenerator ksg = new KeyStoreGenerator();

    private void generateKeyStore(OutputStream outputStream) {
        KeyStore keyStore = ksg.generateKeyStore();
        try {
            keyStore.store(outputStream, KEY_STORE_PASSWORD.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void generateKeyStoreFile(String path) throws IOException {
        generateKeyStore(new FileOutputStream(path));
    }

    public boolean isKeyStoreFileExistent(String path) {
        File file = new File(path);
        return file.exists();
    }
}
