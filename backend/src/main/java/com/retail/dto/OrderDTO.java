package com.retail.dto;
 
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
 
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderDTO {
    private Long id;
    private BigDecimal totalAmount;
    private String status;
    private String deliveryAddress;
    private LocalDateTime createdAt;
    private String couponCode;
    private BigDecimal discountAmount;
    private Integer loyaltyPointsEarned;
    private Integer loyaltyPointsUsed;
    private List<OrderItemDTO> items;
}
 