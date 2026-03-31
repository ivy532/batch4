package com.retail.entity;
 
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
 
@Entity
@Table(name = "order_items")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
 
    @ManyToOne
    @JoinColumn(name = "packaging_id")
    private Packaging packaging;
 
    @Column(nullable = false)
    private Integer quantity;
 
    @Column(nullable = false)
    private BigDecimal priceAtOrder;
}