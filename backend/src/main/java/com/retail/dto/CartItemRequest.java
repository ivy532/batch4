package com.retail.dto;
 
import jakarta.validation.constraints.*;
import lombok.Data;
 
@Data
public class CartItemRequest {
    @NotNull
    private Long packagingId;
    @NotNull @Min(1)
    private Integer quantity;
}