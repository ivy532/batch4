package com.retail.dto;
 
import lombok.*;
import java.math.BigDecimal;
import java.util.List;
 
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String brandName;
    private Long brandId;
    private String categoryName;
    private Long categoryId;
    private List<PackagingDTO> packagingOptions;
}