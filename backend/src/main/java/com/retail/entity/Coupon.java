package com.retail.entity;
 
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
 
@Entity
@Table(name = "coupons")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Coupon {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String code;
 
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
 
    private BigDecimal discountValue;
    private BigDecimal minOrderAmount;
    private LocalDate expiryDate;
    private Integer usageLimit;
    private Integer usedCount = 0;
    private boolean active = true;
 
    public enum DiscountType { PERCENTAGE, FLAT }
}