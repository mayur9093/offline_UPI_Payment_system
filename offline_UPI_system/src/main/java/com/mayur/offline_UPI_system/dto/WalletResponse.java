package com.mayur.offline_UPI_system.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WalletResponse {
    
    private int walletId;

    private int userId;

    private BigDecimal balance;

    private String currency;
}
