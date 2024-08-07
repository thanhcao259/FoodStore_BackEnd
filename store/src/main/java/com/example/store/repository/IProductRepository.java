package com.example.store.repository;

import com.example.store.entity.CartItem;
import com.example.store.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String nameProduct);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    @Query("select p from Product p where p.category.id = ?1 and p.status = ?2")
    List<Product> findByCategoryIdAndStatus(Long idCategory, boolean status);
    @Query("select p from Product p where p.category.id = ?1 and p.status = ?2")
    Page<Product> findByCategoryId(Long cateId, boolean status, Pageable pageable);

    @Query("select p from Product p where p.status = ?1")
    Page<Product> findAllByStatus(boolean status, Pageable pageable);
    @Query("select p from Product p where p.status = ?1")
    List<Product> findAllByStatus(boolean status);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.status =:status")
    List<Product> findByNameAndStatus(@Param("keyword") String keyword, @Param("status") boolean status);
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.status =:status")
    Page<Product> findByNameAndStatus(String keyword, boolean status, Pageable pageable);

    @Query(value = "select c from Category c where c.name like %:name% and c.status =:status")
    List<Product> findByCategoryAndStatus(String name, boolean status);

    @Query(value = "select ci from Order o inner join CartItem ci on o.id = ci.order.id " +
            "inner join Product p on p.id = ci.product.id where o.identity=?1")
    List<CartItem> findAllByOrderAndProduct(String order);
}
