package com.stock.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "stock_detail")
@Getter
@Setter
public class StockDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String stockName;
    private Integer numberOfKitta;        // total bought
    private Double stockPrice;
    private LocalDateTime stockBuyDate;
    private LocalDateTime stockSellDate;
    private String statusOfTheStock; // "BUY" or "SELL" or "PENDING"
    private Integer numberOfKittaToBeSell;
    private Integer numberOfKittaBought;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    // getters/setters
}