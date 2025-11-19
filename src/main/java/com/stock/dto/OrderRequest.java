package com.stock.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequest {
    private String nameOfStock;
    private Double amount;
    private String status; // "buying" or "selling"
    private Integer numberOfKitta;
}

