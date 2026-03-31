package com.retail.entity;
 
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
 
@Entity
@Table(name = "cart")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Cart {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
 
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items;
}