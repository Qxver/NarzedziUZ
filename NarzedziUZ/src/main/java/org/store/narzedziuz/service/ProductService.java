package org.store.narzedziuz.service;

import org.store.narzedziuz.dto.ProductFormDto;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.entity.Tag; // <--- Importuj
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.repository.ProductRepository;
import org.store.narzedziuz.repository.TagRepository; // <--- Importuj
import org.store.narzedziuz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet; // <--- Importuj HashSet
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository; // <--- 1. Wstrzyknięcie repozytorium tagów

    // --- PODSTAWOWE METODY ---

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    @Transactional
    public void updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        productRepository.save(existingProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // --- METODY FORMULARZOWE Z OBSŁUGĄ TAGÓW ---

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

        // <--- 2. Obsługa Tagów (Nowe)
        if (formDto.getTagsInput() != null) {
            Set<Tag> tags = parseAndSaveTags(formDto.getTagsInput());
            product.setTags(tags);
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

        // <--- 3. Obsługa Tagów (Aktualizacja)
        // Uwaga: To nadpisze stare tagi nowymi. Jeśli pole jest puste, usuwa wszystkie tagi.
        if (formDto.getTagsInput() != null) {
            Set<Tag> tags = parseAndSaveTags(formDto.getTagsInput());
            product.setTags(tags);
        }

        productRepository.save(product);
    }

    // --- METODA POMOCNICZA (PRIVATE) ---

    // <--- 4. Logika parsująca tekst na obiekty Tag
    private Set<Tag> parseAndSaveTags(String tagsInput) {
        Set<Tag> tags = new HashSet<>();

        if (tagsInput == null || tagsInput.trim().isEmpty()) {
            return tags;
        }

        String[] tagNames = tagsInput.split(",");

        for (String name : tagNames) {
            String cleanName = name.trim();
            if (!cleanName.isEmpty()) {
                // Znajdź istniejący LUB stwórz nowy
                Tag tag = tagRepository.findByName(cleanName)
                        .orElseGet(() -> {
                            Tag newTag = new Tag();
                            newTag.setName(cleanName);
                            return tagRepository.save(newTag);
                        });
                tags.add(tag);
            }
        }
        return tags;
    }

    // --- TWOJE METODY DO WISHLISTY (bez zmian) ---

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

    public boolean isProductInWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getWishlist().stream()
                .anyMatch(product -> product.getProductId().equals(productId));
    }
}
