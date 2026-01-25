package org.store.narzedziuz.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.repository.ProductRepository;
import org.store.narzedziuz.repository.UserRepository;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new Product();
        testProduct.setProductId(1L);
        testProduct.setName("Młotek");
        testProduct.setPrice(new BigDecimal("29.99"));
        testProduct.setDescription("Profesjonalny młotek");
        testProduct.setQuantity(10);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();

        assertEquals(1, result.size());
        assertEquals("Młotek", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void getProductById_WhenProductExists_ShouldReturnProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Młotek", result.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_WhenProductNotExists_ShouldThrowException() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> productService.getProductById(999L));
    }

    @Test
    void getProductsByCategory_ShouldReturnProductsInCategory() {
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByCategoryCategoryId(1L)).thenReturn(products);

        List<Product> result = productService.getProductsByCategory(1L);

        assertEquals(1, result.size());
        verify(productRepository, times(1)).findByCategoryCategoryId(1L);
    }

    @Test
    void searchProducts_ShouldReturnMatchingProducts() {
        List<Product> products = Arrays.asList(testProduct);
        when(productRepository.findByNameContainingIgnoreCase("młotek")).thenReturn(products);

        List<Product> result = productService.searchProducts("młotek");

        assertEquals(1, result.size());
        assertEquals("Młotek", result.get(0).getName());
    }

    @Test
    void sortProducts_ByPriceAsc_ShouldSortCorrectly() {
        Product cheapProduct = new Product();
        cheapProduct.setProductId(1L);
        cheapProduct.setPrice(new BigDecimal("10.00"));

        Product expensiveProduct = new Product();
        expensiveProduct.setProductId(2L);
        expensiveProduct.setPrice(new BigDecimal("100.00"));

        List<Product> products = Arrays.asList(expensiveProduct, cheapProduct);
        Map<Long, Double> ratings = new HashMap<>();
        Map<Long, Long> reviewCounts = new HashMap<>();

        List<Product> sorted = productService.sortProducts(products, "price-asc", ratings, reviewCounts);

        assertEquals(new BigDecimal("10.00"), sorted.get(0).getPrice());
        assertEquals(new BigDecimal("100.00"), sorted.get(1).getPrice());
    }

    @Test
    void sortProducts_ByPriceDesc_ShouldSortCorrectly() {
        Product cheapProduct = new Product();
        cheapProduct.setProductId(1L);
        cheapProduct.setPrice(new BigDecimal("10.00"));

        Product expensiveProduct = new Product();
        expensiveProduct.setProductId(2L);
        expensiveProduct.setPrice(new BigDecimal("100.00"));

        List<Product> products = Arrays.asList(cheapProduct, expensiveProduct);
        Map<Long, Double> ratings = new HashMap<>();
        Map<Long, Long> reviewCounts = new HashMap<>();

        List<Product> sorted = productService.sortProducts(products, "price-desc", ratings, reviewCounts);

        assertEquals(new BigDecimal("100.00"), sorted.get(0).getPrice());
        assertEquals(new BigDecimal("10.00"), sorted.get(1).getPrice());
    }

    @Test
    void deleteProduct_ShouldCallRepositoryDelete() {
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateProduct_ShouldUpdateExistingProduct() {
        Product updatedProduct = new Product();
        updatedProduct.setName("Nowy Młotek");
        updatedProduct.setPrice(new BigDecimal("39.99"));
        updatedProduct.setDescription("Zaktualizowany opis");
        updatedProduct.setQuantity(20);

        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(Product.class))).thenReturn(testProduct);

        productService.updateProduct(1L, updatedProduct);

        verify(productRepository, times(1)).save(any(Product.class));
    }
}
