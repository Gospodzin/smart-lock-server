package com.stak.smartlockserver.security;

import org.spongycastle.asn1.x500.X500Name;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;
import org.spongycastle.cert.X509v3CertificateBuilder;
import org.spongycastle.cert.jcajce.JcaX509CertificateConverter;
import org.spongycastle.operator.ContentSigner;
import org.spongycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.Date;

import static com.stak.smartlockserver.security.Constants.*;

/**
 * Created by gospo on 28.12.14.
 */
public class KeyStoreGenerator {
    private KeyPair generateKeyPair() {
        KeyPair keyPair = null;

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return keyPair;
    }

    private Certificate generateCertificate(KeyPair keyPair) {
        Certificate certificate = null;

        try {
            X500Name issuer = new X500Name("CN=SmartLock");
            X500Name subject = issuer;
            BigInteger serial = BigInteger.ONE;
            Date notBefore = new Date();
            Date notAfter = new Date(System.currentTimeMillis() + 1000L*60*60*24*365*100);
            SubjectPublicKeyInfo publicKeyInfo = SubjectPublicKeyInfo.getInstance(keyPair.getPublic().getEncoded());
            X509v3CertificateBuilder certBuilder = new X509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, publicKeyInfo);

            ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSA").build(keyPair.getPrivate());
            certificate = new JcaX509CertificateConverter().getCertificate(certBuilder.build(signer));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return certificate;
    }

    private KeyStore createKeyStore(PrivateKey privateKey, Certificate certificate) {
        KeyStore keyStore = null;

        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            keyStore.setKeyEntry("SmartLock", privateKey, KEY_PASSWORD.toCharArray(), new Certificate[]{certificate});

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return keyStore;
    }

    public KeyStore generateKeyStore() {
        KeyPair keyPair = generateKeyPair();
        Certificate certificate = generateCertificate(keyPair);
        KeyStore keyStore = createKeyStore(keyPair.getPrivate(), certificate);

        return keyStore;
    }
}
