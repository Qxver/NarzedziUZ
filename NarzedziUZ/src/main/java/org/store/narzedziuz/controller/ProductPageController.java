package org.store.narzedziuz.controller;

import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.entity.Review;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.ProductService;
import org.store.narzedziuz.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductPageController {
    private final ProductService productService;
    private final ReviewService reviewService;

    @GetMapping("/product/{id}")
    public String productPage(@PathVariable Long id, HttpSession session, Model model) {
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("product", product);

            User user = (User) session.getAttribute("user");

            boolean isLoggedIn = (user != null);
            boolean isInWishlist = false;

            if (isLoggedIn) {
                model.addAttribute("userName", session.getAttribute("userName"));
                isInWishlist = productService.isProductInWishlist(user.getUserId(), id);
            }

            model.addAttribute("isLoggedIn", isLoggedIn);
            model.addAttribute("isInWishlist", isInWishlist);

            List<Review> reviews = reviewService.getProductReviews(id);
            model.addAttribute("reviews", reviews);

            Double averageRating = reviewService.getAverageRating(id);
            long reviewCount = reviewService.getReviewCount(id);

            model.addAttribute("averageRating", averageRating != null ? averageRating : 0.0);
            model.addAttribute("reviewCount", reviewCount);

            return "product";
        } catch (RuntimeException e) {
            return "redirect:/";
        }
    }
}