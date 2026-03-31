package com.retail.dto;
 
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
 
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CartDTO {
    private Long cartId;
    private List<CartItemDTO> items;
    private BigDecimal totalAmount;
}