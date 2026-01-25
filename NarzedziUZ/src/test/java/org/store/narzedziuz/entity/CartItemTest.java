package org.store.narzedziuz.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    void testCartItemCreation() {
        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(1L);
        cartItem.setCartId(5L);
        cartItem.setProductId(10L);
        cartItem.setQuantity(3);
        cartItem.setPrice(new BigDecimal("99.99"));

        assertEquals(1L, cartItem.getCartItemId());
        assertEquals(5L, cartItem.getCartId());
        assertEquals(10L, cartItem.getProductId());
        assertEquals(3, cartItem.getQuantity());
        assertEquals(new BigDecimal("99.99"), cartItem.getPrice());
    }

    @Test
    void testCartItemQuantityUpdate() {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(1);
        
        assertEquals(1, cartItem.getQuantity());
        
        cartItem.setQuantity(5);
        assertEquals(5, cartItem.getQuantity());
    }

    @Test
    void testCartItemPriceUpdate() {
        CartItem cartItem = new CartItem();
        cartItem.setPrice(new BigDecimal("50.00"));
        
        assertEquals(new BigDecimal("50.00"), cartItem.getPrice());
        
        cartItem.setPrice(new BigDecimal("45.00"));
        assertEquals(new BigDecimal("45.00"), cartItem.getPrice());
    }

    @Test
    void testCartItemWithNullValues() {
        CartItem cartItem = new CartItem();
        
        assertNull(cartItem.getCartItemId());
        assertNull(cartItem.getCartId());
        assertNull(cartItem.getProductId());
        assertNull(cartItem.getQuantity());
        assertNull(cartItem.getPrice());
    }
}
