package org.store.narzedziuz.controller;

import org.store.narzedziuz.dto.ProductFormDto;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductFormController {
    private final ProductService productService;

    @GetMapping("/add-product")
    public String showAddProductForm(Model model) {

        log.info("Showing add product form");
        model.addAttribute("product", new ProductFormDto());
        return "add_product";
    }

    @PostMapping("/add-product")
    public String addProduct(@ModelAttribute("product") ProductFormDto productFormDto) {
        log.info("Processing product form: {}", productFormDto.getName());
        try {
            Product product = productService.createProductFromForm(productFormDto);
            log.info("Product created successfully with ID: {}", product.getProductId());
            return "redirect:/add-product?success";
        } catch (Exception e) {
            log.error("Error creating product", e);
            return "redirect:/add-product?error";
        }
    }
}