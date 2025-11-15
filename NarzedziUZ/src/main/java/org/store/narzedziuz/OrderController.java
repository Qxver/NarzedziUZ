package org.store.narzedziuz;

import org.store.narzedziuz.entity.Order;
import org.store.narzedziuz.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestParam Long userId,
            @RequestParam String deliverAddress,
            @RequestParam String billingAddress,
            @RequestParam String paymentMethod) {
        Order order = orderService.createOrderFromCart(
                userId, deliverAddress, billingAddress, paymentMethod);
        return ResponseEntity.ok(order);
    }
}
