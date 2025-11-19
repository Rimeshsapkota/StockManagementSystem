package com.stock.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyKittaRequest {
    private String stockName;       // Name of the stock to verify
    private Integer numberOfKitta;  // Number of kitta user wants to sell
}
