package org.store.narzedziuz.repository;

import org.store.narzedziuz.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // na razie wystarczy, masz już findAll() z JpaRepository
}