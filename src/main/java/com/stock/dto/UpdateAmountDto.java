package com.stock.dto;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UpdateAmountDto {
    private Long userId;
    private Double amount;
    private String transactionType; // CREDIT or DEBIT
    private String description;     // optional (reason for update)
    private Long stockDetailId;
}
