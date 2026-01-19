package org.store.narzedziuz.repository;

import org.store.narzedziuz.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.userId = :userId")
    List<Order> findByUserIdWithItems(@Param("userId") Long userId);
    List<Order> findByOrderStatus(String status);
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.orderId = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);
}