package com.retail.dto;
 
import lombok.*;
import java.math.BigDecimal;
 
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItemDTO {
    private Long packagingId;
    private String productName;
    private String size;
    private String type;
    private Integer quantity;
    private BigDecimal priceAtOrder;
    private BigDecimal subtotal;
}