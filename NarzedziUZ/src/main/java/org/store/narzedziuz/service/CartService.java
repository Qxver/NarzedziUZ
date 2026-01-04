package org.store.narzedziuz.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.store.narzedziuz.entity.Cart;
import org.store.narzedziuz.entity.CartItem;
import org.store.narzedziuz.entity.DiscountCode;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.repository.CartItemRepository;
import org.store.narzedziuz.repository.CartRepository;
import org.store.narzedziuz.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public Cart getOrCreateCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });
    }

    @Transactional
    public CartItem addProductToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Ilość musi być większa niż 0.");
        }

        Cart cart = getOrCreateCartByUserId(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produkt o ID " + productId + " nie znaleziony."));

        Optional<CartItem> existingItemOpt = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst();

        CartItem cartItem;
        if (existingItemOpt.isPresent()) {
            cartItem = existingItemOpt.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setPrice(product.getPrice());
        } else {
            cartItem = new CartItem();
            cartItem.setCartId(cart.getCartId());
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice()); // Ustal cenę
            cartItem.setCart(cart);
            cartItem.setProduct(product);

            cart.getCartItems().add(cartItem);
        }

        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public CartItem updateCartItemQuantity(Long cartItemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Ilość musi być większa niż 0.");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Element koszyka o ID " + cartItemId + " nie znaleziony."));

        cartItem.setQuantity(quantity);

        return cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new RuntimeException("Element koszyka o ID " + cartItemId + " nie znaleziony.");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    public Optional<CartItem> findCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

    public BigDecimal calculateCartTotal(Cart cart, DiscountCode discount) {
        BigDecimal total = cart.getCartItems().stream()
                .map(item -> item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (discount != null) {
            BigDecimal multiplier =
                    BigDecimal.valueOf(100 - discount.getPercent())
                            .divide(BigDecimal.valueOf(100));
            total = total.multiply(multiplier);
        }

        return total;
    }


}