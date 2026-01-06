package org.store.narzedziuz.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tag {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}
