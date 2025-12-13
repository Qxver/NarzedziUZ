package org.store.narzedziuz.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode; // WAŻNE przy relacjach
import lombok.ToString;        // WAŻNE przy relacjach
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;          // <--- Dodaj ten import
import java.util.HashSet;      // <--- Dodaj ten import

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    private String email;
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude          // Zapobiega pętlom przy logowaniu
    @EqualsAndHashCode.Exclude // Zapobiega błędom StackOverflow
    private List<Order> orders;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Review> reviews;

    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cart cart;

    // --- NOWA SEKCJA WISHLIST ---
    @ManyToMany(fetch = FetchType.EAGER) // EAGER ładuje listę od razu, co ułatwi sprawę w prostych projektach
    @JoinTable(
            name = "user_wishlist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Product> wishlist = new HashSet<>();
    // ----------------------------

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
