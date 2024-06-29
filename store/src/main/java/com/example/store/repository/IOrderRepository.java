package com.example.store.repository;

import com.example.store.dto.OrderResponseDTO;
import com.example.store.entity.CartItem;
import com.example.store.entity.Favorite;
import com.example.store.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId (Long userId);
    Optional<Order> findByIdAndUserId (Long id, Long userId);

    @Query("select ci from Order o join CartItem ci on o.id = ci.order.id join Product p on p.id = ci.product.id where o.id = ?1")
    List<CartItem> findCartItemsByOrderAndUsername (Long orderId);
    @Query("select o from Order o where o.id = ?1")
    Optional<Order> findByIdAndUsername(Long id, String username);

}
