package org.store.narzedziuz.service;

import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageLoaderService implements CommandLineRunner {
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        log.info("Starting automatic image loading...");

        // Wczytaj zdjecie produktow z tego katalogu
        String imageDirectory = "src/main/resources/static/product_images";

        try {
            loadImagesFromDirectory(imageDirectory);
            log.info("Image loading completed successfully");
        } catch (Exception e) {
            log.error("Error during image loading: {}", e.getMessage());
        }
    }

    @Transactional
    public void loadImageForProduct(Long productId, String imagePath) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));

        Path path = Paths.get(imagePath);

        if (!Files.exists(path)) {
            log.error("Image file not found: {}", imagePath);
            throw new RuntimeException("Image file not found: " + imagePath);
        }

        String fileName = path.getFileName().toString();
        product.setPhoto(fileName);

        productRepository.save(product);

        log.info("Successfully linked image '{}' to product ID: {}", fileName, productId);
    }

    @Transactional
    public void loadImagesFromDirectory(String directoryPath) {
        try {
            Path dir = Paths.get(directoryPath);

            if (!Files.exists(dir) || !Files.isDirectory(dir)) {
                log.error("Directory not found: {}", directoryPath);
                return;
            }

            Files.list(dir)
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().matches(".*\\.(jpg|jpeg|png)$"))
                    .forEach(path -> {
                        try {
                            String fileName = path.getFileName().toString();
                            String productIdStr = fileName.substring(0, fileName.lastIndexOf('.'));
                            Long productId = Long.parseLong(productIdStr);

                            loadImageForProduct(productId, path.toString());
                        } catch (NumberFormatException e) {
                            log.warn("Skipping file with invalid name format: {}", path.getFileName());
                        } catch (Exception e) {
                            log.error("Error processing file {}: {}", path.getFileName(), e.getMessage());
                        }
                    });

            log.info("Finished loading images from directory: {}", directoryPath);
        } catch (IOException e) {
            log.error("Error reading directory {}: {}", directoryPath, e.getMessage());
            throw new RuntimeException("Failed to load images from directory", e);
        }
    }
}