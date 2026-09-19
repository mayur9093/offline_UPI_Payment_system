package com.mayur.offline_UPI_system.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class OfflineSyncRequest {

    @NotNull(message = "Transaction referance is required")
    private String transactionReference;

    @NotNull(message = "Sender ID is required")
    private Integer senderId;

    @NotBlank(message = "Receiver UPI ID is required")
    private String receiverUpiId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0.01")
    private BigDecimal amount;

    @NotNull(message = "created time is required")
    private LocalDateTime createdAt;

    @NotBlank(message = "nonce is required")
    private String nonce;

    @NotBlank(message = "Signature is required")
    private String signature;

}
