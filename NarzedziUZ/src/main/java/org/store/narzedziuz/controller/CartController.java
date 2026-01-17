package org.store.narzedziuz.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.store.narzedziuz.DiscountCodeProvider;
import org.store.narzedziuz.dto.AddToCartRequest;
import org.store.narzedziuz.entity.Cart;
import org.store.narzedziuz.entity.CartItem;
import org.store.narzedziuz.entity.DiscountCode;
import org.store.narzedziuz.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final DiscountCodeProvider discountCodeProvider;



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
                    .orElseThrow(() -> new RuntimeException("Element koszyka nie znaleziony."));

            if (!item.getCart().getUserId().equals(userId)) {
                redirectAttributes.addFlashAttribute("errorMessage", "Brak uprawnień do usunięcia tego elementu.");
                return "redirect:/cart";
            }
            cartService.removeCartItem(cartItemId);
            redirectAttributes.addFlashAttribute("successMessage", "Produkt usunięty z koszyka.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/apply-discount")
    public String applyDiscount(@RequestParam String code,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        discountCodeProvider.findByCode(code)
                .ifPresentOrElse(
                        discount -> {
                            session.setAttribute("discountCode", discount);
                            redirectAttributes.addFlashAttribute(
                                    "successMessage",
                                    "Kod rabatowy zastosowany: -" + discount.getPercent() + "%"
                            );
                        },
                        () -> redirectAttributes.addFlashAttribute(
                                "errorMessage",
                                "Nieprawidłowy kod rabatowy"
                        )
                );

        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String showCartPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Cart cart = cartService.getOrCreateCartByUserId(userId);

        DiscountCode discount =
                (DiscountCode) session.getAttribute("discountCode");

        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("discount", discount);

        return "cart";
    }


}