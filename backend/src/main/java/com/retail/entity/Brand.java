package com.retail.entity;
 
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
 
@Entity
@Table(name = "brands")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Brand {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String name;
 
    private String description;
    private String logoUrl;
    private boolean active = true;
 
    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Product> products;
}

 