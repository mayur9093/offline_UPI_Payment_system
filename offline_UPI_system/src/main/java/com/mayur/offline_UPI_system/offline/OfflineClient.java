package com.mayur.offline_UPI_system.offline;

import java.util.Base64;
import java.util.Scanner;
import java.util.UUID;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.time.LocalDateTime;

import com.mayur.offline_UPI_system.dto.OfflinePaymentPayload;

public class OfflineClient {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        OfflineStorage offlineStorage = new OfflineStorage();

        System.out.println("Offline upi System");

        System.out.println("Enter the sender Id: ");
        int senderId = sc.nextInt();

        System.out.println("Enter Receiver Id: ");
        int receiverId = sc.nextInt();

        System.out.println("Enter Amount: ");
        BigDecimal amount = sc.nextBigDecimal();

        String transactionReference = "OFF-" + UUID.randomUUID();

        String nonce = UUID.randomUUID().toString();

        LocalDateTime createdAt = LocalDateTime.now();

        String dataToSign = transactionReference + "|" + senderId + "|" + receiverId + "|" + amount.toPlainString()
                + "|" + createdAt + "|" + nonce;

        String signature = sign(dataToSign);

        OfflinePaymentPayload payment = new OfflinePaymentPayload();

        payment.setTransactionReference(transactionReference);
        payment.setSenderId(senderId);
        payment.setReceiverId(receiverId);
        payment.setAmount(amount);
        payment.setCreatedAt(createdAt);
        payment.setNonce(nonce);
        payment.setSignature(signature);

        offlineStorage.savePayment(payment);

        System.out.println("offline payment saved to local storage");

        sc.close();

    }

    private static String sign(String data) {
        try {
            PrivateKey privateKey = OfflineCrypto.getKeyPair().getPrivate();

            Signature signature = Signature.getInstance("SHA256withRSA");

            signature.initSign(privateKey);

            signature.update(data.getBytes(StandardCharsets.UTF_8));

            byte[] signdata = signature.sign();

            return Base64.getEncoder().encodeToString(signdata);

        } catch (Exception e) {

            throw new RuntimeException("Unable to sign payment", e);

        }
    }

}
