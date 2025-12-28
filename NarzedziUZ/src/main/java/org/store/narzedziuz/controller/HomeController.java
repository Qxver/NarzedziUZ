package org.store.narzedziuz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.service.ProductService;
import org.store.narzedziuz.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping("/")
    public String index(
            @RequestParam(required = false) String q,
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

        Map<Long, Double> productRatings = new HashMap<>();
        Map<Long, Long> reviewCounts = new HashMap<>();

        for (Product product : products) {
            Double avgRating = reviewService.getAverageRating(product.getProductId());
            long reviewCount = reviewService.getReviewCount(product.getProductId());

            productRatings.put(product.getProductId(), avgRating != null ? avgRating : 0.0);
            reviewCounts.put(product.getProductId(), reviewCount);
        }

        model.addAttribute("products", products);
        model.addAttribute("productRatings", productRatings);
        model.addAttribute("reviewCounts", reviewCounts);

        return "home";
    }
}