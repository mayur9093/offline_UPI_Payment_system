package com.mayur.offline_UPI_system.offline;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class OfflineCrypto {

    private static String resolveKeyDirectory() {
        if (new File("offline_UPI_system").isDirectory()) {
            return "offline_UPI_system/offline-data";
        }
        return "offline-data";
    }

    private static final String KEY_DIRECTORY = resolveKeyDirectory();

    private static final String PRIVATE_KEY_FILE = KEY_DIRECTORY + "/private.key";

    private static final String PUBLIC_KEY_FILE = KEY_DIRECTORY + "/public.key";

    public static KeyPair getKeyPair() {

        try {

            File directory = new File(KEY_DIRECTORY);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            File privateFile = new File(PRIVATE_KEY_FILE);

            File publicFile = new File(PUBLIC_KEY_FILE);

            // Generate keys only the first time
            if (!privateFile.exists()
                    || !publicFile.exists()) {

                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");

                generator.initialize(2048);

                KeyPair keyPair = generator.generateKeyPair();

                Files.write(
                        Path.of(PRIVATE_KEY_FILE),
                        Base64.getEncoder()
                                .encode(
                                        keyPair.getPrivate()
                                                .getEncoded()));

                Files.write(
                        Path.of(PUBLIC_KEY_FILE),
                        Base64.getEncoder()
                                .encode(
                                        keyPair.getPublic()
                                                .getEncoded()));

                return keyPair;
            }

            // Load existing private key
            byte[] privateBytes = Base64.getDecoder().decode(
                    Files.readAllBytes(
                            Path.of(PRIVATE_KEY_FILE)));

            PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(
                    privateBytes);

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            PrivateKey privateKey = keyFactory.generatePrivate(
                    privateSpec);

            // Load existing public key
            byte[] publicBytes = Base64.getDecoder().decode(
                    Files.readAllBytes(
                            Path.of(PUBLIC_KEY_FILE)));

            X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(
                    publicBytes);

            PublicKey publicKey = keyFactory.generatePublic(
                    publicSpec);

            return new KeyPair(
                    publicKey,
                    privateKey);

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to load offline keys", e);
        }
    }
}