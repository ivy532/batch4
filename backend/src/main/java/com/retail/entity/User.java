package com.retail.entity;
 
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
 
@Entity
@Table(name = "users")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(nullable = false, unique = true)
    private String email;
 
    @Column(nullable = false)
    private String password;
 
    @Column(nullable = false)
    private String name;
 
    private String phone;
 
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
 
    @Column(name = "loyalty_points")
    private Integer loyaltyPoints = 0;
 
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
 
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Order> orders;
 
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;
 
    public enum Role { USER, ADMIN }
}