package org.store.narzedziuz.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testProductCreation() {
        Product product = new Product();
        product.setProductId(1L);
        product.setName("Młotek");
        product.setPrice(new BigDecimal("29.99"));
        product.setDescription("Profesjonalny młotek stolarski");
        product.setQuantity(50);
        product.setManufacturer("Stanley");

        assertEquals(1L, product.getProductId());
        assertEquals("Młotek", product.getName());
        assertEquals(new BigDecimal("29.99"), product.getPrice());
        assertEquals("Profesjonalny młotek stolarski", product.getDescription());
        assertEquals(50, product.getQuantity());
        assertEquals("Stanley", product.getManufacturer());
    }

    @Test
    void testProductSettersAndGetters() {
        Product product = new Product();
        
        product.setName("Wiertarka");
        assertEquals("Wiertarka", product.getName());
        
        product.setPrice(new BigDecimal("199.99"));
        assertEquals(new BigDecimal("199.99"), product.getPrice());
        
        product.setCategoryId(5L);
        assertEquals(5L, product.getCategoryId());
    }

    @Test
    void testProductWithNullValues() {
        Product product = new Product();
        
        assertNull(product.getProductId());
        assertNull(product.getName());
        assertNull(product.getPrice());
        assertNull(product.getDescription());
    }

    @Test
    void testProductEquality() {
        Product product1 = new Product();
        product1.setProductId(1L);
        product1.setName("Śrubokręt");

        Product product2 = new Product();
        product2.setProductId(1L);
        product2.setName("Śrubokręt");

        assertEquals(product1, product2);
    }
}
