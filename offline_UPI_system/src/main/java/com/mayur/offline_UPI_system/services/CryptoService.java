package com.mayur.offline_UPI_system.services;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import org.springframework.stereotype.Service;

@Service
public class CryptoService {

    private final KeyPair keyPair;

    public CryptoService() {
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

            generator.initialize(2048);

            this.keyPair = generator.generateKeyPair();

        } catch (Exception e) {
            throw new RuntimeException("Unable to generate RSA keys " + e);

        }
    }

    public String sign(String data) {
        try {
            PrivateKey privateKey = keyPair.getPrivate();

            Signature signature = Signature.getInstance("SHA256withRSA");

            signature.initSign(privateKey);

            signature.update(data.getBytes(StandardCharsets.UTF_8));

            byte[] signedData = signature.sign();

            return java.util.Base64
                    .getEncoder()
                    .encodeToString(signedData);
        } catch (Exception e) {
            throw new RuntimeException("Unable to sign payment" + e);
        }
    }

    public Boolean verify(String data, String signatureData) {

        try {

            PublicKey publicKey = keyPair.getPublic();

            Signature signature = Signature.getInstance("SHA256withRSA");

            signature.initVerify(publicKey);

            signature.update(data.getBytes(StandardCharsets.UTF_8));

            byte[] decodedSigniture = java.util.Base64.getDecoder().decode(signatureData);

            return signature.verify(decodedSigniture);

        } catch (Exception e) {
            return false;
        }

    }

}
