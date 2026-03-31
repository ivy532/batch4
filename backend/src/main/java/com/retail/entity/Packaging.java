package com.retail.entity;
 
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
 
@Entity
@Table(name = "packaging")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Packaging {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
 
    @Column(nullable = false)
    private String size; // SMALL, MEDIUM, LARGE
 
    @Column(nullable = false)
    private String type; // BOX, BOTTLE, PACK, CAN
 
    @Column(nullable = false)
    private BigDecimal price;
 
    @Column(nullable = false)
    private Integer stockQuantity;
 
    private String description;
}