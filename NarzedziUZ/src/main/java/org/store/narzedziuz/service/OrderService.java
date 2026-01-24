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
    public Order createOrderFromCart(Long userId, String deliverAddress, String billingAddress, String paymentMethod, BigDecimal finalPriceFromForm) {
        // 1. Pobranie usera i koszyka
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // 2. Wstępny zapis zamówienia (żeby dostać ID)
        Order order = new Order();
        order.setUser(user);
        order.setUserId(userId);
        order.setDeliverAddress(deliverAddress);
        order.setBillingAddress(billingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setOrderStatus("NOWE");
        order.setOrderDate(LocalDateTime.now());

        // Zapiszmy, żeby mieć ID do pozycji
        order = orderRepository.save(order);

        BigDecimal total = BigDecimal.ZERO;

        // 3. Przenoszenie produktów
        // Tworzymy tymczasową listę, żeby potem ręcznie wpiąć ją do obiektu Order (dla PDF)
        // JPA i tak to ogarnie przez relację w OrderItem, ale dla pewności w pamięci robimy tak:
        List<OrderItem> newOrderItems = new java.util.ArrayList<>();

        for (CartItem cartItem : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order); // WAŻNE: Ustawiamy relację obiektową
            orderItem.setOrderId(order.getOrderId());
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setProduct(cartItem.getProduct()); // Ustawiamy produkt
            orderItem.setQuantity(cartItem.getQuantity());

            BigDecimal price = cartItem.getProduct().getPrice();
            orderItem.setPrice(price);

            total = total.add(price.multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            // ZAPISUJEMY POZYCJĘ!
            orderItemRepository.save(orderItem);

            newOrderItems.add(orderItem); // Dodajemy do naszej listy
        }

        // 4. Aktualizacja sumy w zamówieniu
        order.setPriceSum(total);
        if (finalPriceFromForm != null && finalPriceFromForm.compareTo(BigDecimal.ZERO) > 0) {
            order.setPriceSum(finalPriceFromForm);
        }

        // RĘCZNE WPIĘCIE LISTY DO OBIEKTU (Dla pewności przed Flushem)
        order.setOrderItems(newOrderItems);

        // 5. FLUSH - Wysyłamy wszystko do bazy
        // To zapisze zaktualizowaną cenę ORAZ upewni się, że OrderItems są w bazie
        orderRepository.saveAndFlush(order);

        // 6. Czyszczenie koszyka
        cart.getCartItems().clear();
        cartRepository.save(cart);

        // 7. Pobieramy "Full Order" dla PDF
        // Dzięki flushowi wyżej, to zapytanie zwróci komplet danych
        Order fullOrder = orderRepository.findByIdWithItems(order.getOrderId())
                .orElse(order);

        // Zabezpieczenie Usera (czasem fetch może nie zaciągnąć usera w tym samym query)
        if (fullOrder.getUser() == null) fullOrder.setUser(user);

        // 8. Generowanie PDF i Wysyłka
        try {
            byte[] invoicePdf = pdfService.generateInvoicePdf(fullOrder);

            emailService.sendEmailWithAttachment(
                    user.getEmail(),
                    "Potwierdzenie zamówienia nr " + fullOrder.getOrderId(),
                    "Dziękujemy za zakupy! W załączniku przesyłamy fakturę VAT.",
                    invoicePdf,
                    "Faktura_" + fullOrder.getOrderId() + ".pdf"
            );
        } catch (Exception e) {
            // Logujemy, ale nie przerywamy procesu zamówienia
            e.printStackTrace();
        }

        return fullOrder;
    }

    public Order getOrderWithDetails(Long orderId) {
        return orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new RuntimeException("Zamówienie nie istnieje"));
    }
}
