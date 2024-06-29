package com.example.store.repository;

import com.example.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String nameProduct);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    Page<Product> findByCategoryId(Long idCategory, Pageable pageable);
    List<Product> findByCategoryId(Long idCategory);


    @Query("select p from Product p where p.status = ?1")
    List<Product> findAllByStatus(boolean status);
}
