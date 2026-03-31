package com.retail.repository;
 
import com.retail.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
 
public interface CategoryRepository extends JpaRepository<Category, Long> {}