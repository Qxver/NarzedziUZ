package org.store.narzedziuz.service;

import org.store.narzedziuz.entity.*;
import org.store.narzedziuz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository; // <-- DODANO TO
    private final PdfService pdfService;
    private final EmailService emailService;

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserIdWithItems(userId);
    }

    @Transactional
    public Order createOrderFromCart(Long userId, String deliverAddress,
                                     String billingAddress, String paymentMethod) {
        // 1. Pobranie użytkownika (WAŻNE DO PDF)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Pobranie koszyka
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 3. Tworzenie zamówienia
        Order order = new Order();
        order.setUser(user);         // <-- TUTAJ NAPRAWA: Przypisujemy obiekt User
        order.setUserId(userId);     // Ustawiamy też ID dla pewności
        order.setDeliverAddress(deliverAddress);
        order.setBillingAddress(billingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setOrderStatus("NOWE");
        order.setOrderDate(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        order = orderRepository.save(order);

        // 4. Przenoszenie produktów
        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getOrderId());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());

            BigDecimal price = cartItem.getProduct().getPrice();
            orderItem.setPrice(price);
            orderItem.setProduct(cartItem.getProduct()); // Ustawiamy produkt dla PDF

            orderItemRepository.save(orderItem);

            total = total.add(price.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        // 5. Czyszczenie koszyka
        cart.getCartItems().clear();
        cartRepository.save(cart);

        // 6. Aktualizacja sumy
        order.setPriceSum(total);
        orderRepository.save(order);

        // 7. Pobranie pełnego obiektu
        Order fullOrder = getOrderWithDetails(order.getOrderId());

        // ZABEZPIECZENIE: Jeśli repozytorium nie dociągnęło usera, ustawiamy go ręcznie
        if (fullOrder.getUser() == null) {
            fullOrder.setUser(user);
        }

        // ==================================================================
        // 8. WYSYŁKA MAILA
        // ==================================================================
        try {
            System.out.println(">>> Generowanie PDF dla: " + fullOrder.getOrderId());
            byte[] invoicePdf = pdfService.generateInvoicePdf(fullOrder);

            System.out.println(">>> Wysyłka maila do: " + user.getEmail());
            emailService.sendEmailWithAttachment(
                    user.getEmail(),
                    "Potwierdzenie zamówienia nr " + fullOrder.getOrderId(),
                    "Dziękujemy za zakupy! W załączniku przesyłamy fakturę VAT.",
                    invoicePdf,
                    "Faktura_" + fullOrder.getOrderId() + ".pdf"
            );

        } catch (Exception e) {
            System.err.println(">>> BŁĄD WYSYŁKI MAILA: " + e.getMessage());
            e.printStackTrace();
        }

        return fullOrder;
    }

    public Order getOrderWithDetails(Long orderId) {
        return orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Zamówienie nie istnieje"));
    }
}
