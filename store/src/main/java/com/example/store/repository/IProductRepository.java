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
    @Query("select p from Product p where p.category.id = ?1 and p.status = ?2")
    List<Product> findByCategoryIdAndStatus(Long idCategory, boolean status);
    Page<Product> findByCategoryId(Long cateId, Pageable pageable);


    @Query("select p from Product p where p.status = ?1")
    List<Product> findAllByStatus(boolean status);

    @Query("select p from Product p where p.name like %:keyword% and p.status = :status")
    List<Product> findByNameAndStatus(String keyword, boolean status);
    Page<Product> findByNameAndStatusTrue(String keyword, Pageable pageable);
}
