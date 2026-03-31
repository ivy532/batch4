package com.retail.controller;
 
import com.retail.dto.*;
import com.retail.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
 
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Browse products")
public class ProductController {
 
    private final ProductService productService;
 
    @GetMapping
    @Operation(summary = "Get all products")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(productService.getAllProducts()));
    }
 
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    public ResponseEntity<ApiResponse<ProductDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getById(id)));
    }
 
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Get products by category")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getByCategory(categoryId)));
    }
 
    @GetMapping("/brand/{brandId}")
    @Operation(summary = "Get products by brand")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getByBrand(@PathVariable Long brandId) {
        return ResponseEntity.ok(ApiResponse.success(productService.getByBrand(brandId)));
    }
 
    @GetMapping("/search")
    @Operation(summary = "Search products")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(productService.search(keyword)));
    }
}