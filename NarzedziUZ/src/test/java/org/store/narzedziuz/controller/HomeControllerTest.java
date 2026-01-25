package org.store.narzedziuz.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.store.narzedziuz.entity.Category;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.repository.CategoryRepository;
import org.store.narzedziuz.service.ProductService;
import org.store.narzedziuz.service.ReviewService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private CategoryRepository categoryRepository;

    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setName("Młotek");
        testProduct.setPrice(new BigDecimal("29.99"));
        testProduct.setQuantity(10);

        testCategory = new Category();
        testCategory.setCategoryId(1L);
        testCategory.setName("Narzędzia ręczne");
    }

    @Test
    void homePage_ShouldReturnHomeView() throws Exception {
        List<Product> products = Arrays.asList(testProduct);
        when(productService.getAllProducts()).thenReturn(products);
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(testCategory));
        when(reviewService.getAverageRating(anyLong())).thenReturn(4.5);
        when(reviewService.getReviewCount(anyLong())).thenReturn(10L);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("products"));
    }

    @Test
    void homePage_WithSearchQuery_ShouldFilterProducts() throws Exception {
        List<Product> products = Arrays.asList(testProduct);
        when(productService.searchProducts("młotek")).thenReturn(products);
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(testCategory));
        when(reviewService.getAverageRating(anyLong())).thenReturn(4.5);
        when(reviewService.getReviewCount(anyLong())).thenReturn(10L);

        mockMvc.perform(get("/").param("q", "młotek"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("searchPerformed", true))
                .andExpect(model().attribute("searchQuery", "młotek"));
    }

    @Test
    void homePage_WithCategoryId_ShouldFilterByCategory() throws Exception {
        List<Product> products = Arrays.asList(testProduct);
        when(productService.getProductsByCategory(1L)).thenReturn(products);
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(testCategory));
        when(reviewService.getAverageRating(anyLong())).thenReturn(4.5);
        when(reviewService.getReviewCount(anyLong())).thenReturn(10L);

        mockMvc.perform(get("/").param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attribute("searchPerformed", false));
    }
}
