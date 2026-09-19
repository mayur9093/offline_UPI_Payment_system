package com.mayur.offline_UPI_system.offline;

import java.util.List;
import java.util.Scanner;

import com.mayur.offline_UPI_system.dto.OfflinePaymentPayload;

public class OfflineSyncClient {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        OfflineStorage offlineStorage = new OfflineStorage();

        List<OfflinePaymentPayload> payments = offlineStorage.loadPayments();

        if (payments.isEmpty()) {
            System.out.println("No pending offline payments.");

            sc.close();
            return;
        }

        System.out.println("OFFLINE PAYMENT SYNC");

        System.out.println("Pending payments: " + payments.size());

    }

}
