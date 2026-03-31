package com.retail.dto;
 
import lombok.Data;
 
@Data
public class OrderRequest {
    private String deliveryAddress;
    private String couponCode;
    private Integer loyaltyPointsToUse;
}
 