package org.store.narzedziuz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.service.ProductService;
import org.store.narzedziuz.repository.CategoryRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sortBy,
            HttpSession session,
            Model model) {

        Object user = session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", session.getAttribute("userName"));
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        List<Product> products;

        // Search product by name
        if (q != null && !q.trim().isEmpty()) {
            products = productService.searchProducts(q.trim());
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchQuery", q.trim());
        } else {
            products = productService.getAllProducts();
            model.addAttribute("searchPerformed", false);
            model.addAttribute("searchQuery", "");
        }

        model.addAttribute("products", products);

        return "home";
    }
}