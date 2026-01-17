package org.store.narzedziuz.service;

import org.store.narzedziuz.dto.ProductFormDto;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.repository.ProductRepository;
import org.store.narzedziuz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        return productRepository.findByCategoryCategoryId(categoryId);
    }

    public List<Product> searchProducts(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    /**
     * Sort products based on the selected criteria
     * @param products List of products to sort
     * @param sortBy Sorting criteria: "price-asc", "price-desc", "rating-asc", "rating-desc"
     * @param productRatings Map of product IDs to their average ratings
     * @param reviewCounts Map of product IDs to their review counts
     * @return Sorted list of products
     */
    public List<Product> sortProducts(List<Product> products, String sortBy,
                                      Map<Long, Double> productRatings,
                                      Map<Long, Long> reviewCounts) {
        return switch (sortBy) {
            case "price-asc" -> products.stream()
                    .sorted(Comparator.comparing(Product::getPrice))
                    .collect(Collectors.toList());

            case "price-desc" -> products.stream()
                    .sorted(Comparator.comparing(Product::getPrice).reversed())
                    .collect(Collectors.toList());

            case "rating-asc" -> products.stream()
                    .sorted(Comparator
                            .comparing((Product p) -> reviewCounts.getOrDefault(p.getProductId(), 0L) > 0 ? 0 : 1)
                            .thenComparingDouble(p -> productRatings.getOrDefault(p.getProductId(), 0.0)))
                    .collect(Collectors.toList());

            case "rating-desc" -> products.stream()
                    .sorted(Comparator
                            .comparing((Product p) -> reviewCounts.getOrDefault(p.getProductId(), 0L) > 0 ? 0 : 1)
                            .thenComparing(Comparator.comparingDouble((Product p) ->
                                    productRatings.getOrDefault(p.getProductId(), 0.0)).reversed()))
                    .collect(Collectors.toList());

            default -> products;
        };
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

    @Transactional
    public Product createProductFromForm(ProductFormDto formDto) {
        Product product = new Product();
        product.setName(formDto.getName());
        product.setPrice(formDto.getPrice());
        product.setDescription(formDto.getDescription());
        product.setQuantity(formDto.getQuantity());
        product.setCategoryId(formDto.getCategoryId());
        product.setManufacturer(formDto.getManufacturer());

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

    public boolean isProductInWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getWishlist().stream()
                .anyMatch(product -> product.getProductId().equals(productId));
    }
}