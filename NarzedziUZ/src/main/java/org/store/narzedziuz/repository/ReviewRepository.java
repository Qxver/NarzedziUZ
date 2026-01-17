package org.store.narzedziuz.repository;

import org.store.narzedziuz.entity.Review;
import org.store.narzedziuz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // Wszystkie oceny danego użytkownika
    List<Review> findByUser(User user);

    // Wszystkie oceny danego produktu, posortowane od najnowszej oceny
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);

    // Sprawdzenie czy user już ocenił produkt
    boolean existsByUserIdAndProductId(Long userId, Long productId);

    // Znajdź opinię użytkownika dla konkretnego produktu
    Review findByUserIdAndProductId(Long userId, Long productId);

    // Liczba ocen danego produktu
    long countByProductId(Long productId);

    // Średnia ocena produktu
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productId = :productId")
    Double getAverageRatingByProductId(@Param("productId") Long productId);
}