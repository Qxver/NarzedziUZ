package org.store.narzedziuz.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddToCartRequestTest {

    @Test
    void testAddToCartRequestCreation() {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(5);

        assertEquals(1L, request.getProductId());
        assertEquals(5, request.getQuantity());
    }

    @Test
    void testAddToCartRequestWithNullValues() {
        AddToCartRequest request = new AddToCartRequest();

        assertNull(request.getProductId());
        assertNull(request.getQuantity());
    }

    @Test
    void testAddToCartRequestSettersAndGetters() {
        AddToCartRequest request = new AddToCartRequest();

        request.setProductId(100L);
        assertEquals(100L, request.getProductId());

        request.setQuantity(10);
        assertEquals(10, request.getQuantity());
    }
}
