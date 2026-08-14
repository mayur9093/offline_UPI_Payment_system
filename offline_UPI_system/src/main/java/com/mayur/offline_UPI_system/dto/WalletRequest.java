package com.mayur.offline_UPI_system.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WalletRequest {

    @NotNull(message = "User id cannot be blank")
    private Integer userId;

    @DecimalMin(value = "0.0", inclusive = true, message = "balance cannot be negative")
    private BigDecimal balance;

    @NotBlank(message = "currency is required")
    private String currency;

}
