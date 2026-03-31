package com.retail.controller;
 
import com.retail.dto.*;
import com.retail.entity.*;
import com.retail.repository.*;
import com.retail.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
 
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Admin operations")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
 
    private final ProductService productService;
    private final OrderService orderService;
    private final CouponService couponService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
 
    @GetMapping("/orders")
    @Operation(summary = "Get all orders (Admin)")
    public ResponseEntity<ApiResponse<List<OrderDTO>>> getAllOrders() {
        return ResponseEntity.ok(ApiResponse.success(orderService.getAllOrders()));
    }
 
    @PutMapping("/orders/{id}/status")
    @Operation(summary = "Update order status")
    public ResponseEntity<ApiResponse<OrderDTO>> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(ApiResponse.success(orderService.updateStatus(id, status)));
    }
 
    @PostMapping("/products")
    @Operation(summary = "Add new product")
    public ResponseEntity<ApiResponse<ProductDTO>> addProduct(@RequestBody ProductDTO dto) {
        return ResponseEntity.ok(ApiResponse.success(productService.createProduct(dto)));
    }
 
    @PostMapping("/categories")
    @Operation(summary = "Add new category")
    public ResponseEntity<ApiResponse<Category>> addCategory(@RequestBody Category category) {
        return ResponseEntity.ok(ApiResponse.success(categoryRepository.save(category)));
    }
 
    @PostMapping("/brands")
    @Operation(summary = "Add new brand")
    public ResponseEntity<ApiResponse<Brand>> addBrand(@RequestBody Brand brand) {
        return ResponseEntity.ok(ApiResponse.success(brandRepository.save(brand)));
    }
 
    @PostMapping("/coupons")
    @Operation(summary = "Create coupon")
    public ResponseEntity<ApiResponse<Coupon>> createCoupon(@RequestBody Coupon coupon) {
        return ResponseEntity.ok(ApiResponse.success(couponService.createCoupon(coupon)));
    }
}