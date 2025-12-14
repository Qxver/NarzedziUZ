package org.store.narzedziuz.repository;

import org.store.narzedziuz.entity.Review;
import org.store.narzedziuz.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // wszystkie oceny danego użytkownika
    List<Review> findByUser(User user);
}

