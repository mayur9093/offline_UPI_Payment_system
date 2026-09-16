package com.mayur.offline_UPI_system.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "offline_Transections")
@Getter
@Setter
public class OfflineTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String transactionReference;

    @Column(nullable = false)
    private int senderId;

    @Column(nullable = false)
    private int receiverId;

    @Column(nullable = false)
    private BigDecimal Amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OfflineTransactionStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

}
