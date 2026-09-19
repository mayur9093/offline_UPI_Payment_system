package com.mayur.offline_UPI_system.offline;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mayur.offline_UPI_system.dto.OfflinePaymentPayload;

public class OfflineSyncClient {

    private static final String SYNC_URL = "http://localhost:8080/offline-payments/sync";

    public static void main(String[] args) {

        OfflineStorage offlineStorage = new OfflineStorage();

        List<OfflinePaymentPayload> payments = offlineStorage.loadPayments();

        if (payments.isEmpty()) {
            System.out.println("No pending offline payments.");
            return;
        }

        String token;

        try {
            // JWT is taken automatically from local session
            token = OfflineSession.getToken();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(
                new JavaTimeModule());

        HttpClient client = HttpClient.newHttpClient();

        for (OfflinePaymentPayload payment : payments) {

            try {

                String json = objectMapper.writeValueAsString(payment);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(SYNC_URL))
                        .header(
                                "Content-Type",
                                "application/json")
                        .header(
                                "Authorization",
                                "Bearer " + token)
                        .POST(
                                HttpRequest.BodyPublishers
                                        .ofString(json))
                        .build();

                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

                System.out.println(
                        "Transaction: "
                                + payment.getTransactionReference());

                System.out.println(
                        "Status: "
                                + response.statusCode());

                System.out.println(
                        "Response: "
                                + response.body());

                if (response.statusCode() >= 200
                        && response.statusCode() < 300) {

                    offlineStorage.removePayment(
                            payment.getTransactionReference());

                    System.out.println(
                            "Payment synced successfully.");
                } else {

                    System.out.println(
                            "Payment was not synced. "
                                    + "Keeping it in local storage.");
                }

            } catch (Exception e) {

                System.out.println(
                        "Unable to sync transaction: "
                                + payment.getTransactionReference());

                System.out.println(
                        "Reason: " + e.getMessage());
            }
        }
    }
}