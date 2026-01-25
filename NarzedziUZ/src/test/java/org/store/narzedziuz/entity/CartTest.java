package org.store.narzedziuz.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    @Test
    void testCartCreation() {
        Cart cart = new Cart();
        cart.setCartId(1L);
        cart.setUserId(10L);

        assertEquals(1L, cart.getCartId());
        assertEquals(10L, cart.getUserId());
    }

    @Test
    void testCartItemsInitialization() {
        Cart cart = new Cart();
        
        assertNotNull(cart.getCartItems());
        assertTrue(cart.getCartItems().isEmpty());
    }

    @Test
    void testAddCartItem() {
        Cart cart = new Cart();
        cart.setCartId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(1L);
        cartItem.setProductId(5L);
        cartItem.setQuantity(2);
        cartItem.setPrice(new BigDecimal("49.99"));

        cart.getCartItems().add(cartItem);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(cartItem, cart.getCartItems().get(0));
    }

    @Test
    void testCartWithMultipleItems() {
        Cart cart = new Cart();

        CartItem item1 = new CartItem();
        item1.setCartItemId(1L);
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setCartItemId(2L);
        item2.setQuantity(3);

        cart.getCartItems().add(item1);
        cart.getCartItems().add(item2);

        assertEquals(2, cart.getCartItems().size());
    }
}
