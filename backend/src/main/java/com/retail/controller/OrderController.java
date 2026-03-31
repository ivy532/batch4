package com.retail.controller;
 
import com.retail.dto.*;
import com.retail.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
 
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {
 
    private final OrderService orderService;
 
    @PostMapping("/place")
    @Operation(summary = "Place a new order")
    public ResponseEntity<ApiResponse<OrderDTO>> placeOrder(@AuthenticationPrincipal UserDetails user,
                                                             @RequestBody OrderRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Order placed!", orderService.placeOrder(user.getUsername(), request)));
    }
 
    @GetMapping("/my")
    @Operation(summary = "Get my order history")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> myOrders(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getUserOrders(user.getUsername())));
    }
 
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<OrderDTO>> getOrder(@PathVariable Long id,
                                                           @AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(ApiResponse.success(orderService.getOrderById(id, user.getUsername())));
    }
 
    @PostMapping("/{id}/reorder")
    @Operation(summary = "Reorder a previous order")
    public ResponseEntity<ApiResponse<String>> reorder(@PathVariable Long id,
                                                        @AuthenticationPrincipal UserDetails user) {
        orderService.reorder(id, user.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Items added to cart!", null));
    }
}