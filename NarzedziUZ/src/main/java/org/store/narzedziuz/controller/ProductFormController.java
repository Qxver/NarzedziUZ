package org.store.narzedziuz.controller;

import org.store.narzedziuz.dto.ProductFormDto;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.ProductService;
import org.store.narzedziuz.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductFormController {
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/product_image/";

    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {
        log.info("Showing add product form");
        model.addAttribute("product", new ProductFormDto());
        model.addAttribute("categories", categoryRepository.findAll());
        return "add_product";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("product") ProductFormDto productFormDto) {
        log.info("Processing product form: {}", productFormDto.getName());
        try {
            handleImageUpload(productFormDto);
            Product product = productService.createProductFromForm(productFormDto);
            log.info("Product created successfully with ID: {}", product.getProductId());
            return "redirect:/add-product?success";
        } catch (Exception e) {
            log.error("Error creating product", e);
            return "redirect:/add-product?error";
        }
    }

    @GetMapping("/admin/products/add")
    public String showAdminAddProductForm(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        model.addAttribute("product", new ProductFormDto());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("isEdit", false);
        return "add_product";
    }

    @PostMapping("/admin/products/add")
    public String adminAddProduct(@ModelAttribute("product") ProductFormDto productFormDto, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        try {
            handleImageUpload(productFormDto);
            Product product = productService.createProductFromForm(productFormDto);
            log.info("Product created successfully with ID: {}", product.getProductId());
            return "redirect:/admin/products?added";
        } catch (Exception e) {
            log.error("Error creating product", e);
            return "redirect:/admin/products/add?error";
        }
    }

    @GetMapping("/admin/products/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        Product product = productService.getProductById(id);
        ProductFormDto dto = new ProductFormDto();
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setCategoryId(product.getCategoryId());
        dto.setManufacturer(product.getManufacturer());
        
        model.addAttribute("product", dto);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("productId", id);
        model.addAttribute("currentPhoto", product.getPhoto());
        model.addAttribute("isEdit", true);
        return "add_product";
    }

    @PostMapping("/admin/products/edit/{id}")
    public String updateProduct(@PathVariable Long id, 
                               @ModelAttribute("product") ProductFormDto productFormDto,
                               HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        try {
            handleImageUpload(productFormDto);
            productService.updateProductFromForm(id, productFormDto);
            return "redirect:/admin/products?updated";
        } catch (Exception e) {
            log.error("Error updating product", e);
            return "redirect:/admin/products/edit/" + id + "?error";
        }
    }

    private void handleImageUpload(ProductFormDto productFormDto) throws IOException {
        MultipartFile image = productFormDto.getImage();
        if (image != null && !image.isEmpty()) {
            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String filename = UUID.randomUUID().toString() + extension;
            
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            Path filePath = uploadPath.resolve(filename);
            Files.write(filePath, image.getBytes());
            
            // Update DTO with the filename (not the full path)
            productFormDto.setImageFilename(filename);
            log.info("Image uploaded successfully: {}", filename);
        }
    }
}