package com.stock.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_history")
@Getter
@Setter
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type; // "LOAD", "BUY", "SELL"
    private Double amount;
    private Integer numberOfKitta;
    private LocalDateTime timestamp;
    private String details;
    @ManyToOne @JoinColumn(name="user_id")
    private User user;
    // getters/setters
}
