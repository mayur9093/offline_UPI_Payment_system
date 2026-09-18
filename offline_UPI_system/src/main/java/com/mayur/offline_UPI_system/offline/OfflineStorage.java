package com.mayur.offline_UPI_system.offline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mayur.offline_UPI_system.dto.OfflinePaymentPayload;

@Component
public class OfflineStorage {

    private final ObjectMapper objectMapper;

    private static File resolveStorageFile() {
        if (new File("offline_UPI_system").isDirectory()) {
            return new File("offline_UPI_system/offline-data/pendingPayments.json");
        }
        return new File("offline-data/pendingPayments.json");
    }

    private final File storageFile = resolveStorageFile();

    public OfflineStorage() {
        this.objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

    }

    public void savePayment(OfflinePaymentPayload payment) {
        try {
            File directory = storageFile.getParentFile();

            if (directory != null && !directory.exists()) {
                directory.mkdirs();
            }

            List<OfflinePaymentPayload> payments = loadPayments();

            payments.add(payment);

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(storageFile, payments);
        } catch (Exception e) {
            throw new RuntimeException("Unable to save offline Payment", e);
        }
    }

    public List<OfflinePaymentPayload> loadPayments() {

        try {

            if (!storageFile.exists()) {
                return new ArrayList<>();
            }

            OfflinePaymentPayload[] payments = objectMapper.readValue(storageFile, OfflinePaymentPayload[].class);

            return new ArrayList<>(Arrays.asList(payments));

        } catch (Exception e) {
            throw new RuntimeException("Unable to read payments file");
        }

    }

    public void clearPayments() {
        try {
            if (storageFile.exists()) {
                objectMapper.writeValue(storageFile, new ArrayList<>());
            }
        } catch (Exception e) {
            throw new RuntimeException("Unable to clear payments file");
        }

    }

}
