package com.retail.controller;
 
import com.retail.dto.*;
import com.retail.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
 
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Cart management")
@SecurityRequirement(name = "bearerAuth")
public class CartController {
 
    private final CartService cartService;
 
    @GetMapping
    @Operation(summary = "Get user cart")
    public ResponseEntity<ApiResponse<CartDTO>> getCart(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(ApiResponse.success(cartService.getCart(user.getUsername())));
    }
 
    @PostMapping("/add")
    @Operation(summary = "Add item to cart")
    public ResponseEntity<ApiResponse<CartDTO>> addItem(@AuthenticationPrincipal UserDetails user,
                                                         @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success(cartService.addItem(user.getUsername(), request)));
    }
 
    @PutMapping("/item/{cartItemId}")
    @Operation(summary = "Update cart item quantity")
    public ResponseEntity<ApiResponse<CartDTO>> updateItem(@AuthenticationPrincipal UserDetails user,
                                                            @PathVariable Long cartItemId,
                                                            @RequestParam Integer quantity) {
        return ResponseEntity.ok(ApiResponse.success(cartService.updateItem(user.getUsername(), cartItemId, quantity)));
    }
 
    @DeleteMapping("/item/{cartItemId}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<ApiResponse<Void>> removeItem(@PathVariable Long cartItemId) {
        cartService.removeItem(cartItemId);
        return ResponseEntity.ok(ApiResponse.success("Item removed", null));
    }
}