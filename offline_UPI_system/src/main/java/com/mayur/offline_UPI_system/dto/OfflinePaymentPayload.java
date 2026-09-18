package com.mayur.offline_UPI_system.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OfflinePaymentPayload {

    private String transactionReference;

    private Integer senderId;

    private Integer receiverId;

    private BigDecimal amount;

    private LocalDateTime createdAt;

    private String nonce;

    private String signature;

}
