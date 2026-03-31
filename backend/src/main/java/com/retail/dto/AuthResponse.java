package com.retail.dto;
 
import lombok.*;
 
@Data @AllArgsConstructor
public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private String role;
    private Integer loyaltyPoints;
}