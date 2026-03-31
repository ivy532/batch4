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
public class OrderService {
 
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final PackagingRepository packagingRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final EmailService emailService;
    private final CartService cartService;
 
    @Transactional
    public OrderDTO placeOrder(String email, OrderRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Cart is empty"));
 
        if (cart.getItems() == null || cart.getItems().isEmpty())
            throw new RuntimeException("Cart is empty");
 
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
 
        for (CartItem ci : cart.getItems()) {
            Packaging pkg = packagingRepository.findById(ci.getPackaging().getId()).orElseThrow();
            if (pkg.getStockQuantity() < ci.getQuantity())
                throw new RuntimeException("Insufficient stock for: " + pkg.getProduct().getName());
 
            pkg.setStockQuantity(pkg.getStockQuantity() - ci.getQuantity());
            packagingRepository.save(pkg);
 
            BigDecimal subtotal = pkg.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity()));
            total = total.add(subtotal);
 
            orderItems.add(OrderItem.builder()
                    .packaging(pkg).quantity(ci.getQuantity()).priceAtOrder(pkg.getPrice()).build());
        }
 
        BigDecimal discountAmount = BigDecimal.ZERO;
        String couponCode = null;
 
        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            Coupon coupon = couponRepository.findByCodeAndActiveTrue(request.getCouponCode())
                    .orElseThrow(() -> new RuntimeException("Invalid coupon"));
            if (total.compareTo(coupon.getMinOrderAmount()) >= 0) {
                if (coupon.getDiscountType() == Coupon.DiscountType.PERCENTAGE)
                    discountAmount = total.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));
                else
                    discountAmount = coupon.getDiscountValue();
                couponCode = coupon.getCode();
                coupon.setUsedCount(coupon.getUsedCount() + 1);
                couponRepository.save(coupon);
            }
        }
 
        int pointsUsed = 0;
        if (request.getLoyaltyPointsToUse() != null && request.getLoyaltyPointsToUse() > 0) {
            pointsUsed = Math.min(request.getLoyaltyPointsToUse(), user.getLoyaltyPoints());
            BigDecimal pointsValue = BigDecimal.valueOf(pointsUsed).divide(BigDecimal.valueOf(10));
            discountAmount = discountAmount.add(pointsValue);
            user.setLoyaltyPoints(user.getLoyaltyPoints() - pointsUsed);
        }
 
        total = total.subtract(discountAmount).max(BigDecimal.ZERO);
        int pointsEarned = total.intValue() / 10;
        user.setLoyaltyPoints(user.getLoyaltyPoints() + pointsEarned);
        userRepository.save(user);
 
        Order order = Order.builder()
                .user(user).totalAmount(total).status(Order.OrderStatus.CONFIRMED)
                .deliveryAddress(request.getDeliveryAddress()).couponCode(couponCode)
                .discountAmount(discountAmount).loyaltyPointsUsed(pointsUsed)
                .loyaltyPointsEarned(pointsEarned).build();
 
        order = orderRepository.save(order);
        for (OrderItem item : orderItems) { item.setOrder(order); }
        order.setItems(orderItems);
        order = orderRepository.save(order);
 
        cartService.clearCart(user.getId());
        emailService.sendOrderConfirmation(user.getEmail(), user.getName(), order);
 
        return toDTO(order);
    }
 
    public List<OrderDTO> getUserOrders(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return orderRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public OrderDTO getOrderById(Long id, String email) {
        Order order = orderRepository.findById(id).orElseThrow();
        return toDTO(order);
    }
 
    @Transactional
    public OrderDTO reorder(Long orderId, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Order original = orderRepository.findById(orderId).orElseThrow();
        Cart cart = cartRepository.findByUserId(user.getId()).orElseGet(() ->
            cartRepository.save(Cart.builder().user(user).items(new ArrayList<>()).build()));
        for (OrderItem item : original.getItems()) {
            cartService.addItem(email, new CartItemRequest() {{
                setPackagingId(item.getPackaging().getId());
                setQuantity(item.getQuantity());
            }});
        }
        return null;
    }
 
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc().stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public OrderDTO updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(Order.OrderStatus.valueOf(status));
        return toDTO(orderRepository.save(order));
    }
 
    private OrderDTO toDTO(Order o) {
        List<OrderItemDTO> items = o.getItems() == null ? List.of() :
            o.getItems().stream().map(oi -> OrderItemDTO.builder()
                .packagingId(oi.getPackaging().getId())
                .productName(oi.getPackaging().getProduct().getName())
                .size(oi.getPackaging().getSize())
                .type(oi.getPackaging().getType())
                .quantity(oi.getQuantity())
                .priceAtOrder(oi.getPriceAtOrder())
                .subtotal(oi.getPriceAtOrder().multiply(BigDecimal.valueOf(oi.getQuantity())))
                .build()).collect(Collectors.toList());
 
        return OrderDTO.builder().id(o.getId()).totalAmount(o.getTotalAmount())
                .status(o.getStatus().name()).deliveryAddress(o.getDeliveryAddress())
                .createdAt(o.getCreatedAt()).couponCode(o.getCouponCode())
                .discountAmount(o.getDiscountAmount()).loyaltyPointsEarned(o.getLoyaltyPointsEarned())
                .loyaltyPointsUsed(o.getLoyaltyPointsUsed()).items(items).build();
    }
}