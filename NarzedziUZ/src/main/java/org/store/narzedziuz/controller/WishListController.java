package org.store.narzedziuz.controller;

import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class WishListController {

    private final ProductService productService;

    @GetMapping("/wish-list")
    public String getWishList(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("user"); // Zakładam, że w sesji trzymasz obiekt User
        if (sessionUser == null) {
            return "redirect:/login";
        }

        // Pobierz aktualną listę z bazy
        Set<Product> wishlist = productService.getUserWishlist(sessionUser.getUserId());

        model.addAttribute("wishlistItems", wishlist);
        // ... reszta atrybutów ...
        return "wishlist";
    }

    @PostMapping("/wish-list/add/{productId}")
    public String addProduct(@PathVariable Long productId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser == null) return "redirect:/login";

        productService.addToWishlist(sessionUser.getUserId(), productId);

        // Zmiana: powrót na stronę produktu
        return "redirect:/product/" + productId;
    }


    @PostMapping("/wish-list/remove/{productId}")
    public String removeProduct(@PathVariable Long productId, HttpSession session) {
        User sessionUser = (User) session.getAttribute("user");
        if (sessionUser != null) {
            productService.removeFromWishlist(sessionUser.getUserId(), productId);
        }
        return "redirect:/user/wish-list";
    }
}
