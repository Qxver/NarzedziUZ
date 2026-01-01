package org.store.narzedziuz.service;

import org.store.narzedziuz.dto.ProductFormDto;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.repository.ProductRepository;
import org.store.narzedziuz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // --- PODSTAWOWE METODY ---

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // --- METODY WYMAGANE PRZEZ KONTROLER (te, których brakowało) ---

    public List<Product> getProductsByCategory(Long categoryId) {
        // Upewnij się, że w ProductRepository masz: List<Product> findByCategoryCategoryId(Long id);
        return productRepository.findByCategoryCategoryId(categoryId);
    }

    public List<Product> searchProducts(String query) {
        // Upewnij się, że w ProductRepository masz: List<Product> findByNameContainingIgnoreCase(String name);
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    @Transactional
    public void updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);

        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        // existingProduct.setCategory(updatedProduct.getCategory()); // odkomentuj jeśli aktualizujesz kategorię

        productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Transactional
    public Product createProductFromForm(ProductFormDto formDto) {
        Product product = new Product();
        product.setName(formDto.getName());
        product.setPrice(formDto.getPrice());
        product.setDescription(formDto.getDescription());
        product.setQuantity(formDto.getQuantity());
        product.setCategoryId(formDto.getCategoryId());
        product.setManufacturer(formDto.getManufacturer());

        // Obsługa zdjęcia
        if (formDto.getImageFilename() != null && !formDto.getImageFilename().isEmpty()) {
            product.setPhoto(formDto.getImageFilename());
        }

        return productRepository.save(product);
    }

    @Transactional
    public void updateProductFromForm(Long id, ProductFormDto formDto) {
        Product product = getProductById(id);
        product.setName(formDto.getName());
        product.setPrice(formDto.getPrice());
        product.setDescription(formDto.getDescription());
        product.setQuantity(formDto.getQuantity());
        product.setCategoryId(formDto.getCategoryId());
        product.setManufacturer(formDto.getManufacturer());

        // Update photo only if a new one was uploaded
        if (formDto.getImageFilename() != null && !formDto.getImageFilename().isEmpty()) {
            product.setPhoto(formDto.getImageFilename());
        }

        productRepository.save(product);
    }

    // --- TWOJE METODY DO WISHLISTY ---

    @Transactional
    public void addToWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = getProductById(productId);

        user.getWishlist().add(product);
        userRepository.save(user);
    }

    @Transactional
    public void removeFromWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = getProductById(productId);

        user.getWishlist().remove(product);
        userRepository.save(user);
    }

    public Set<Product> getUserWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getWishlist();
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    // W ProductService.java
    public boolean isProductInWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Sprawdzamy czy w zestawie wishlisty istnieje produkt o danym ID
        return user.getWishlist().stream()
                .anyMatch(product -> product.getProductId().equals(productId));
    }

}
