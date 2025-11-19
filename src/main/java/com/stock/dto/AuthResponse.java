package com.stock.dto;

import lombok.*;

@Getter
@Setter

public class AuthResponse {
    private String token;
    private String message;
    private Long userId;

    public AuthResponse(String token) {
        this.token=token;
    }
}
