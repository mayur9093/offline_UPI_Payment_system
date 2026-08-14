package com.mayur.offline_UPI_system.model;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "wallets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private BigDecimal balance;

    private String currency;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
