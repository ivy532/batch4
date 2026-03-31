package com.retail.dto;
 
import lombok.*;
import java.math.BigDecimal;
 
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemDTO {
    private Long cartItemId;
    private Long packagingId;
    private String productName;
    private String size;
    private String type;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
    private String imageUrl;
}