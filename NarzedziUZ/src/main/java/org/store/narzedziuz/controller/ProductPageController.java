package org.store.narzedziuz.controller;

import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ProductPageController {
    private final ProductService productService;

    @GetMapping("/product/{id}")
    public String productPage(@PathVariable Long id, HttpSession session, Model model) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);

            Object user = session.getAttribute("user");
            if (user != null) {
                model.addAttribute("userName", session.getAttribute("userName"));
                model.addAttribute("isLoggedIn", true);
            } else {
                model.addAttribute("isLoggedIn", false);
            }

            return "product";
        } catch (RuntimeException e) {
            return "redirect:/";
        }
    }
}