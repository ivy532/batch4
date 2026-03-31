package com.retail.dto;
 
import lombok.*;
import java.math.BigDecimal;
 
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PackagingDTO {
    private Long id;
    private String size;
    private String type;
    private BigDecimal price;
    private Integer stockQuantity;
    private String description;
}