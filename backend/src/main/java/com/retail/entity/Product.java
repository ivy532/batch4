package com.retail.entity;
 
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
 
@Entity
@Table(name = "products")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false)
    private String name;
 
    private String description;
    private String imageUrl;
    private boolean active = true;
 
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;
 
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
 
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Packaging> packagingOptions;
} 

