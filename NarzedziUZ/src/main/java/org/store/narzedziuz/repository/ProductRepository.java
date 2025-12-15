package org.store.narzedziuz.repository;

import org.store.narzedziuz.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryId(Long categoryId);

    @Query("SELECT p FROM Product p WHERE p.quantity > 0")
    List<Product> findAvailableProducts();

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByCategoryCategoryId(Long categoryId);
}