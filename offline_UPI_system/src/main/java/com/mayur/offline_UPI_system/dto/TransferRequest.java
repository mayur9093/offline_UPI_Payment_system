package com.mayur.offline_UPI_system.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequest {

    @NotNull(message = "Sender ID is required")
    private Integer senderId;

    @NotNull(message = "Receiver ID is required")
    private Integer receiverId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.1", message = "Amount should be greater than zero")
    private BigDecimal amount;

}
