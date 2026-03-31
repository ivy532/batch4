package com.retail.dto;
 
import lombok.*;
import java.math.BigDecimal;
 
@Data @AllArgsConstructor
public class CouponValidateResponse {
    private boolean valid;
    private String message;
    private BigDecimal discountAmount;
}