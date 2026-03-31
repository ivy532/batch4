package com.retail.service;
 
import com.retail.dto.*;
import com.retail.entity.*;
import com.retail.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
 
@Service
@RequiredArgsConstructor
public class CartService {
 
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PackagingRepository packagingRepository;
    private final UserRepository userRepository;
 
    public CartDTO getCart(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            Cart c = Cart.builder().user(user).items(new ArrayList<>()).build();
            return cartRepository.save(c);
        });
        return toDTO(cart);
    }
 
    @Transactional
    public CartDTO addItem(String email, CartItemRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() -> {
            Cart c = Cart.builder().user(user).items(new ArrayList<>()).build();
            return cartRepository.save(c);
        });
        Packaging packaging = packagingRepository.findById(request.getPackagingId()).orElseThrow();
        Optional<CartItem> existing = cartItemRepository.findByCartIdAndPackagingId(cart.getId(), packaging.getId());
        if (existing.isPresent()) {
            existing.get().setQuantity(existing.get().getQuantity() + request.getQuantity());
            cartItemRepository.save(existing.get());
        } else {
            CartItem item = CartItem.builder().cart(cart).packaging(packaging).quantity(request.getQuantity()).build();
            cartItemRepository.save(item);
        }
        return toDTO(cartRepository.findById(cart.getId()).orElseThrow());
    }
 
    @Transactional
    public CartDTO updateItem(String email, Long cartItemId, Integer quantity) {
        CartItem item = cartItemRepository.findById(cartItemId).orElseThrow();
        if (quantity <= 0) cartItemRepository.delete(item);
        else { item.setQuantity(quantity); cartItemRepository.save(item); }
        User user = userRepository.findByEmail(email).orElseThrow();
        return toDTO(cartRepository.findByUserId(user.getId()).orElseThrow());
    }
 
    @Transactional
    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }
 
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.findByUserId(userId).ifPresent(cart -> {
            cart.getItems().clear();
            cartRepository.save(cart);
        });
    }
 
    private CartDTO toDTO(Cart cart) {
        List<CartItemDTO> items = cart.getItems() == null ? List.of() :
            cart.getItems().stream().map(ci -> CartItemDTO.builder()
                .cartItemId(ci.getId())
                .packagingId(ci.getPackaging().getId())
                .productName(ci.getPackaging().getProduct().getName())
                .size(ci.getPackaging().getSize())
                .type(ci.getPackaging().getType())
                .price(ci.getPackaging().getPrice())
                .quantity(ci.getQuantity())
                .subtotal(ci.getPackaging().getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())))
                .imageUrl(ci.getPackaging().getProduct().getImageUrl())
                .build()).collect(Collectors.toList());
 
        BigDecimal total = items.stream().map(CartItemDTO::getSubtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return CartDTO.builder().cartId(cart.getId()).items(items).totalAmount(total).build();
    }
}
 