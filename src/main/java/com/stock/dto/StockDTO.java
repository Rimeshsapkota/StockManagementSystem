package com.stock.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StockDTO {
    private Long id;
    private String stockName;
    private Double stockPrice;
    private LocalDateTime stockBuyDate;
    private LocalDateTime stockSellDate;
    private Long userId;
    private String statusOfTheStock;       // BUY or SELL
    private Integer numberOfKitta;
    private Integer numberOfKittaToBeSell;
    private Double price;
}
