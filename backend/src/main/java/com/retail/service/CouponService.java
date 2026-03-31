package com.retail.service;
 
import com.retail.dto.CouponValidateResponse;
import com.retail.entity.Coupon;
import com.retail.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
 
@Service
@RequiredArgsConstructor
public class CouponService {
 
    private final CouponRepository couponRepository;
 
    public CouponValidateResponse validate(String code, BigDecimal orderAmount) {
        return couponRepository.findByCodeAndActiveTrue(code).map(coupon -> {
            if (coupon.getExpiryDate() != null && coupon.getExpiryDate().isBefore(LocalDate.now()))
                return new CouponValidateResponse(false, "Coupon expired", BigDecimal.ZERO);
            if (orderAmount.compareTo(coupon.getMinOrderAmount()) < 0)
                return new CouponValidateResponse(false, "Minimum order ₹" + coupon.getMinOrderAmount() + " required", BigDecimal.ZERO);
            BigDecimal discount = coupon.getDiscountType() == Coupon.DiscountType.PERCENTAGE
                    ? orderAmount.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100))
                    : coupon.getDiscountValue();
            return new CouponValidateResponse(true, "Coupon applied! You save ₹" + discount, discount);
        }).orElse(new CouponValidateResponse(false, "Invalid coupon code", BigDecimal.ZERO));
    }
 
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }
}
 