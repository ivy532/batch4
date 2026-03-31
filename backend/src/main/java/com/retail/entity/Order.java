package com.retail.entity;
 
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
 
@Entity
@Table(name = "orders")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
 
    @Column(nullable = false)
    private BigDecimal totalAmount;
 
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;
 
    private String deliveryAddress;
 
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
 
    @Column(name = "coupon_code")
    private String couponCode;
 
    @Column(name = "discount_amount")
    private BigDecimal discountAmount = BigDecimal.ZERO;
 
    @Column(name = "loyalty_points_used")
    private Integer loyaltyPointsUsed = 0;
 
    @Column(name = "loyalty_points_earned")
    private Integer loyaltyPointsEarned = 0;
 
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items;
 
    public enum OrderStatus {
        PENDING, CONFIRMED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED
    }
}