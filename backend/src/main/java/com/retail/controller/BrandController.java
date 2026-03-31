package com.retail.controller;
 
import com.retail.entity.Brand;
import com.retail.repository.BrandRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.retail.dto.ApiResponse;
import java.util.List;
 
@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
@Tag(name = "Brands")
public class BrandController {
 
    private final BrandRepository brandRepository;
 
    @GetMapping
    public ResponseEntity<ApiResponse<List<Brand>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(brandRepository.findByActiveTrue()));
    }
}