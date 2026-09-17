package com.mayur.offline_UPI_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfflinePaymentPayload {

    private String transactionReference;

    private int senderId;

    private int receiverId;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    private String nonce;

    private String signature;

}
