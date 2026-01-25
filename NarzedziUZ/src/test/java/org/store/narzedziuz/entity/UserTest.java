package org.store.narzedziuz.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testUserCreation() {
        User user = new User();
        user.setUserId(1L);
        user.setEmail("jan.kowalski@example.com");
        user.setPassword("hashedPassword123");
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setRole("USER");

        assertEquals(1L, user.getUserId());
        assertEquals("jan.kowalski@example.com", user.getEmail());
        assertEquals("hashedPassword123", user.getPassword());
        assertEquals("Jan", user.getFirstName());
        assertEquals("Kowalski", user.getLastName());
        assertEquals("USER", user.getRole());
    }

    @Test
    void testUserSettersAndGetters() {
        User user = new User();

        user.setEmail("test@test.pl");
        assertEquals("test@test.pl", user.getEmail());

        user.setFirstName("Anna");
        assertEquals("Anna", user.getFirstName());

        user.setLastName("Nowak");
        assertEquals("Nowak", user.getLastName());

        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    void testUserWithNullValues() {
        User user = new User();

        assertNull(user.getUserId());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
    }

    @Test
    void testUserWishlistInitialization() {
        User user = new User();
        
        assertNotNull(user.getWishlist());
        assertTrue(user.getWishlist().isEmpty());
    }
}
