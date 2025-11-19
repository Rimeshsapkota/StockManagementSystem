package com.stock.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users") // "user" is reserved in some dbs
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username; // email or username for login
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer numberOfKitta;
    private String forgetPasswordCode;
    private LocalDateTime forgetPasswordCodeExpiry;
    private String roles; // e.g. "ROLE_USER"
    // getters/setters, constructors
}