package org.store.narzedziuz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.store.narzedziuz.entity.Tag;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    // Metoda do szukania tagu po nazwie
    Optional<Tag> findByName(String name);
}