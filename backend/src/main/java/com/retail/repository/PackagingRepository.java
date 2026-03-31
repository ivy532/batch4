package com.retail.repository;
 
import com.retail.entity.Packaging;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
 
public interface PackagingRepository extends JpaRepository<Packaging, Long> {
    List<Packaging> findByProductId(Long productId);
}