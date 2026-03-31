package com.retail.controller;
 
import com.retail.dto.*;
import com.retail.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
 
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupons", description = "Coupon management")
@SecurityRequirement(name = "bearerAuth")
public class CouponController {
 
    private final CouponService couponService;
 
    @GetMapping("/validate")
    @Operation(summary = "Validate a coupon")
    public ResponseEntity<ApiResponse<CouponValidateResponse>> validate(
            @RequestParam String code, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(ApiResponse.success(couponService.validate(code, amount)));
    }
}