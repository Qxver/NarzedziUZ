package org.store.narzedziuz.controller;

import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.entity.User; // <--- Dodany import
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

            // Pobieramy usera z sesji
            User user = (User) session.getAttribute("user");

            boolean isLoggedIn = (user != null);
            boolean isInWishlist = false;

            if (isLoggedIn) {
                model.addAttribute("userName", session.getAttribute("userName")); // lub user.getFirstName()

                // --- NOWA LOGIKA WISHLISTY ---
                // Sprawdzamy czy produkt jest na liście tego usera
                isInWishlist = productService.isProductInWishlist(user.getUserId(), id);
            }

            model.addAttribute("isLoggedIn", isLoggedIn);
            model.addAttribute("isInWishlist", isInWishlist); // Przekazujemy flagę do widoku

            return "product"; // Upewnij się, że Twój plik HTML nazywa się product.html
        } catch (RuntimeException e) {
            return "redirect:/";
        }
    }
}
