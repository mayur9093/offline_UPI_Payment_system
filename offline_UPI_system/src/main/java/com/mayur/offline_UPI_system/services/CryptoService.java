package com.mayur.offline_UPI_system.services;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.mayur.offline_UPI_system.offline.OfflineCrypto;

@Service
public class CryptoService {

    private final KeyPair keyPair;

    public CryptoService() {

        this.keyPair = OfflineCrypto.getKeyPair();
    }

    public String sign(String data) {

        try {

            PrivateKey privateKey = keyPair.getPrivate();

            Signature signature = Signature.getInstance(
                    "SHA256withRSA");

            signature.initSign(privateKey);

            signature.update(
                    data.getBytes(
                            StandardCharsets.UTF_8));

            byte[] signedData = signature.sign();

            return Base64.getEncoder()
                    .encodeToString(signedData);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to sign payment", e);
        }
    }

    public boolean verify(
            String data,
            String signatureData) {

        try {

            PublicKey publicKey = keyPair.getPublic();

            Signature signature = Signature.getInstance(
                    "SHA256withRSA");

            signature.initVerify(publicKey);

            signature.update(
                    data.getBytes(
                            StandardCharsets.UTF_8));

            byte[] decodedSignature = Base64.getDecoder()
                    .decode(signatureData);

            return signature.verify(
                    decodedSignature);

        } catch (Exception e) {

            return false;
        }
    }
}