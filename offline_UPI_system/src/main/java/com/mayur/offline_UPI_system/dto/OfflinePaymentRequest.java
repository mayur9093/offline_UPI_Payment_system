package com.mayur.offline_UPI_system.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfflinePaymentRequest {

    @NotNull(message = "Receiver ID is required")
    private Integer ReciverId;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.01", message = "Amount must be greater than Zero")
    private BigDecimal amount;

}
