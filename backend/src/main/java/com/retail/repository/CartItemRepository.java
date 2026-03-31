package com.retail.repository;
 
import com.retail.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
 
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartIdAndPackagingId(Long cartId, Long packagingId);
}