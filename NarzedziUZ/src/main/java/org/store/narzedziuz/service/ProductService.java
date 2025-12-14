package org.store.narzedziuz.service;

import org.store.narzedziuz.dto.ProductFormDto;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = getProductById(id);
        product.setName(productDetails.getName());
        product.setPrice(productDetails.getPrice());
        product.setDescription(productDetails.getDescription());
        product.setQuantity(productDetails.getQuantity());
        return productRepository.save(product);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public Product createProductFromForm(ProductFormDto productFormDto) {
        Product product = new Product();
        product.setName(productFormDto.getName());
        product.setPrice(productFormDto.getPrice());
        product.setDescription(productFormDto.getDescription());
        product.setQuantity(productFormDto.getQuantity());
        product.setCategoryId(productFormDto.getCategoryId());
        product.setManufacturer(productFormDto.getManufacturer());

        if (productFormDto.getImage() != null && !productFormDto.getImage().isEmpty()) {
            product.setPhoto(productFormDto.getImage().getOriginalFilename());
        }

        return productRepository.save(product);
    }
}