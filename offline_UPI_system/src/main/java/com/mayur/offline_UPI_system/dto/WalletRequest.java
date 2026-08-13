package com.mayur.offline_UPI_system.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletRequest {
    @DecimalMin(value = "0.0", inclusive = true, message = "balabce cannot be negative")
    private double balance;

    @NotBlank(message = "currency is required")
    private String currency;

}
