package org.store.narzedziuz.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void testCategoryCreation() {
        Category category = new Category();
        category.setCategoryId(1L);
        category.setName("Narzędzia ręczne");
        category.setDescription("Różne narzędzia ręczne");

        assertEquals(1L, category.getCategoryId());
        assertEquals("Narzędzia ręczne", category.getName());
        assertEquals("Różne narzędzia ręczne", category.getDescription());
    }

    @Test
    void testCategorySettersAndGetters() {
        Category category = new Category();

        category.setName("Elektronarzędzia");
        assertEquals("Elektronarzędzia", category.getName());

        category.setDescription("Narzędzia elektryczne");
        assertEquals("Narzędzia elektryczne", category.getDescription());
    }

    @Test
    void testCategoryWithNullValues() {
        Category category = new Category();

        assertNull(category.getCategoryId());
        assertNull(category.getName());
        assertNull(category.getDescription());
    }
}
