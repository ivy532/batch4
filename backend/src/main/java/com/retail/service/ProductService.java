package com.retail.service;
 
import com.retail.dto.*;
import com.retail.entity.*;
import com.retail.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
 
@Service
@RequiredArgsConstructor
public class ProductService {
 
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final PackagingRepository packagingRepository;
 
    public List<ProductDTO> getAllProducts() {
        return productRepository.findByActiveTrue().stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public List<ProductDTO> getByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId).stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public List<ProductDTO> getByBrand(Long brandId) {
        return productRepository.findByBrandId(brandId).stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public ProductDTO getById(Long id) {
        return productRepository.findById(id).map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
 
    public List<ProductDTO> search(String keyword) {
        return productRepository.searchProducts(keyword).stream().map(this::toDTO).collect(Collectors.toList());
    }
 
    public ProductDTO createProduct(ProductDTO dto) {
        Brand brand = brandRepository.findById(dto.getBrandId()).orElseThrow();
        Category category = categoryRepository.findById(dto.getCategoryId()).orElseThrow();
        Product product = Product.builder()
                .name(dto.getName()).description(dto.getDescription())
                .imageUrl(dto.getImageUrl()).brand(brand).category(category).active(true).build();
        return toDTO(productRepository.save(product));
    }
 
    public ProductDTO toDTO(Product p) {
        return ProductDTO.builder()
                .id(p.getId()).name(p.getName()).description(p.getDescription())
                .imageUrl(p.getImageUrl()).brandName(p.getBrand().getName()).brandId(p.getBrand().getId())
                .categoryName(p.getCategory().getName()).categoryId(p.getCategory().getId())
                .packagingOptions(p.getPackagingOptions() != null ?
                        p.getPackagingOptions().stream().map(pkg -> PackagingDTO.builder()
                                .id(pkg.getId()).size(pkg.getSize()).type(pkg.getType())
                                .price(pkg.getPrice()).stockQuantity(pkg.getStockQuantity())
                                .description(pkg.getDescription()).build())
                        .collect(Collectors.toList()) : List.of())
                .build();
    }
}