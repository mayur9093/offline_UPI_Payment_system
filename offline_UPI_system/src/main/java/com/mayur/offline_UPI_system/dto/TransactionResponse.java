package com.mayur.offline_UPI_system.dto;

import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.mayur.offline_UPI_system.model.TransactionStatus;

@Getter
@Setter
public class TransactionResponse {

    private int transactionId;

    private int senderId;

    private int receiverId;

    private BigDecimal amount;

    private TransactionStatus Status;

    private LocalDateTime createdAt;

}
