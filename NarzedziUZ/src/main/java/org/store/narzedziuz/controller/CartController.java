package org.store.narzedziuz.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.store.narzedziuz.dto.AddToCartRequest;
import org.store.narzedziuz.entity.Cart;
import org.store.narzedziuz.entity.CartItem;
import org.store.narzedziuz.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping("/cart")
    public String showCartPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        Cart cart = cartService.getOrCreateCartByUserId(userId);
        model.addAttribute("cartItems", cart.getCartItems());
        return "cart";
    }

    @PostMapping("/api/cart/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }
        try {
            CartItem cartItem = cartService.addProductToCart(userId, request.getProductId(), request.getQuantity());
            return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/cart/remove/{cartItemId}")
    public String removeCartItem(@PathVariable Long cartItemId, HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        try {
            CartItem item = cartService.findCartItemById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            if (!item.getCart().getUserId().equals(userId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "You cannot delete this item.");
                return "redirect:/cart";
            }
            cartService.removeCartItem(cartItemId);
            redirectAttributes.addFlashAttribute("successMessage", "Product deleted from cart.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart";
    }
}