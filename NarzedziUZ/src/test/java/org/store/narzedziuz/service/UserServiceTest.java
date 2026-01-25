package org.store.narzedziuz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedPassword");
        testUser.setFirstName("Jan");
        testUser.setLastName("Kowalski");
        testUser.setRole("USER");
    }

    @Test
    void registerUser_WithValidData_ShouldCreateUser() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.registerUser(
                "test@example.com",
                "Password1!",
                "Jan",
                "Kowalski"
        );

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_WithExistingEmail_ShouldThrowException() {
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                userService.registerUser("existing@example.com", "Password1!", "Jan", "Kowalski")
        );
    }

    @Test
    void registerUser_WithEmptyEmail_ShouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                userService.registerUser("", "Password1!", "Jan", "Kowalski")
        );
    }

    @Test
    void registerUser_WithNullEmail_ShouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                userService.registerUser(null, "Password1!", "Jan", "Kowalski")
        );
    }

    @Test
    void registerUser_WithInvalidEmailFormat_ShouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                userService.registerUser("invalid-email", "Password1!", "Jan", "Kowalski")
        );
    }

    @Test
    void registerUser_WithShortPassword_ShouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                userService.registerUser("test@example.com", "Pass1!", "Jan", "Kowalski")
        );
    }

    @Test
    void registerUser_WithWeakPassword_ShouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                userService.registerUser("test@example.com", "password", "Jan", "Kowalski")
        );
    }

    @Test
    void registerUser_WithEmptyFirstName_ShouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                userService.registerUser("test@example.com", "Password1!", "", "Kowalski")
        );
    }

    @Test
    void registerUser_WithEmptyLastName_ShouldThrowException() {
        assertThrows(RuntimeException.class, () ->
                userService.registerUser("test@example.com", "Password1!", "Jan", "")
        );
    }

    @Test
    void loginUser_WithValidCredentials_ShouldReturnUser() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("Password1!", "hashedPassword")).thenReturn(true);

        User result = userService.loginUser("test@example.com", "Password1!");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
    }

    @Test
    void loginUser_WithInvalidEmail_ShouldThrowException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                userService.loginUser("nonexistent@example.com", "Password1!")
        );
    }

    @Test
    void loginUser_WithInvalidPassword_ShouldThrowException() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                userService.loginUser("test@example.com", "wrongPassword")
        );
    }
}
