package org.store.narzedziuz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.narzedziuz.entity.Cart;
import org.store.narzedziuz.entity.CartItem;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.repository.CartItemRepository;
import org.store.narzedziuz.repository.CartRepository;
import org.store.narzedziuz.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    private Cart testCart;
    private Product testProduct;
    private CartItem testCartItem;

    @BeforeEach
    void setUp() {
        testCart = new Cart();
        testCart.setCartId(1L);
        testCart.setUserId(1L);
        testCart.setCartItems(new ArrayList<>());

        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setName("Młotek");
        testProduct.setPrice(new BigDecimal("29.99"));
        testProduct.setQuantity(10);

        testCartItem = new CartItem();
        testCartItem.setCartItemId(1L);
        testCartItem.setCartId(1L);
        testCartItem.setProductId(1L);
        testCartItem.setQuantity(2);
        testCartItem.setPrice(new BigDecimal("29.99"));
    }

    @Test
    void getOrCreateCartByUserId_WhenCartExists_ShouldReturnExistingCart() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));

        Cart result = cartService.getOrCreateCartByUserId(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCartId());
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void getOrCreateCartByUserId_WhenCartNotExists_ShouldCreateNewCart() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.getOrCreateCartByUserId(1L);

        assertNotNull(result);
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void addProductToCart_WithValidData_ShouldAddItem() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        CartItem result = cartService.addProductToCart(1L, 1L, 2);

        assertNotNull(result);
        assertEquals(2, result.getQuantity());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void addProductToCart_WithZeroQuantity_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                cartService.addProductToCart(1L, 1L, 0)
        );
    }

    @Test
    void addProductToCart_WithNegativeQuantity_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                cartService.addProductToCart(1L, 1L, -1)
        );
    }

    @Test
    void addProductToCart_WithNonExistentProduct_ShouldThrowException() {
        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(testCart));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                cartService.addProductToCart(1L, 999L, 1)
        );
    }

    @Test
    void updateCartItemQuantity_WithValidQuantity_ShouldUpdate() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(testCartItem);

        CartItem result = cartService.updateCartItemQuantity(1L, 5);

        assertEquals(5, result.getQuantity());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

    @Test
    void updateCartItemQuantity_WithZeroQuantity_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () ->
                cartService.updateCartItemQuantity(1L, 0)
        );
    }

    @Test
    void updateCartItemQuantity_WithNonExistentItem_ShouldThrowException() {
        when(cartItemRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                cartService.updateCartItemQuantity(999L, 5)
        );
    }

    @Test
    void removeCartItem_WithExistingItem_ShouldRemove() {
        when(cartItemRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cartItemRepository).deleteById(1L);

        cartService.removeCartItem(1L);

        verify(cartItemRepository, times(1)).deleteById(1L);
    }

    @Test
    void removeCartItem_WithNonExistentItem_ShouldThrowException() {
        when(cartItemRepository.existsById(999L)).thenReturn(false);

        assertThrows(RuntimeException.class, () ->
                cartService.removeCartItem(999L)
        );
    }

    @Test
    void findCartItemById_WhenExists_ShouldReturnItem() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(testCartItem));

        Optional<CartItem> result = cartService.findCartItemById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getCartItemId());
    }

    @Test
    void findCartItemById_WhenNotExists_ShouldReturnEmpty() {
        when(cartItemRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<CartItem> result = cartService.findCartItemById(999L);

        assertTrue(result.isEmpty());
    }
}
