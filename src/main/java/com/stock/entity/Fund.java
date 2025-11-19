package com.stock.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "fund")
@Getter
@Setter
public class Fund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double loadAmountInTheStock;
    private Double deductedAmountAfterBuyingStock;
    private LocalDateTime createdAt;
    @ManyToOne @JoinColumn(name="user_id")
    private User user;
    @ManyToOne @JoinColumn(name="stock_detail_id")
    private StockDetail stockDetail;
    // getters/setters
}