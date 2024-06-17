package com.example.store.repository;

import com.example.store.entity.CartItem;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICartItemRepository extends JpaRepository<CartItem, Long> {
//    List<CartItem> findByCartIdAndDeletedIs (Long cartId, boolean isDeleted);
    @Query("select c from CartItem c where c.cart.id=:cartId and c.isDeleted = false")
    List<CartItem> findAllByCartIdAndIsDeleted (Long cartId);
//    Optional<CartItem> findByCartIdAndProductIdAndDeletedIs(Long cartId, Long productId, boolean isDeleted);
    @Query("select c from CartItem c where c.cart.id = :cartId and c.product.id = :productId and c.isDeleted = false ")
    Optional<CartItem> findByCartIdAndProductAndIsDeleted(@Param("cartId") Long cartId, @Param("productId") Long productId);
//    List<CartItem> findByCartIdAndDeletedIsOrderById(Long cartId, boolean isDeleted);
//    List<CartItem> findByCartIdAndProductId (Long cartId, Long productId);
}
