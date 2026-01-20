package org.store.narzedziuz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.store.narzedziuz.dto.OrderFormDto;
import org.store.narzedziuz.entity.Order;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.EmailService;
import org.store.narzedziuz.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.store.narzedziuz.service.PdfService;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final PdfService pdfService; // To pole jest już poprawnie wstrzykiwane przez @RequiredArgsConstructor

    @PostMapping("/process-shipping")
    public String processShipping(@ModelAttribute OrderFormDto form, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        // 1. Adres dostawy
        String fullAddress = String.format("%s %s, %s %s, %s",
                form.getStreet() != null ? form.getStreet() : "",
                form.getHouseNumber() != null ? form.getHouseNumber() : "",
                form.getZipCode() != null ? form.getZipCode() : "",
                form.getCity() != null ? form.getCity() : "",
                form.getCountry() != null ? form.getCountry() : ""
        ).trim();

        // 2. Budowanie ładnego opisu płatności (Bezpieczniej i czytelniej)
        String paymentDetails = form.getPaymentMethod(); // Domyślnie np. "BLIK", "PAYPAL"

        switch (form.getPaymentMethod()) {
            case "BLIK":
                // Nie zapisujemy kodu BLIK (jest jednorazowy i tajny), tylko informację
                paymentDetails = "BLIK (Platnosc mobilna)";
                break;

            case "CARD":
                String cardNum = form.getCardNumber();
                if (cardNum != null && cardNum.length() >= 4) {
                    String last4 = cardNum.substring(cardNum.length() - 4);
                    paymentDetails = "Karta Platnicza (**** " + last4 + ")";
                } else {
                    paymentDetails = "Karta Platnicza";
                }
                break;

            case "PAYPAL":
                // Dla PayPal zazwyczaj zapisuje się ID transakcji, tu uproszczamy
                paymentDetails = "PayPal (Online)";
                break;

            case "TRANSFER":
                paymentDetails = "Przelew Tradycyjny";
                break;

            default:
                paymentDetails = "Inna: " + form.getPaymentMethod();
                break;
        }

        try {
            // 3. Tworzymy zamówienie
            Order savedOrder = orderService.createOrderFromCart(
                    user.getUserId(),
                    fullAddress,
                    fullAddress,
                    paymentDetails // Teraz do bazy trafi ładny opis
            );

            return "redirect:/user/purchase/" + savedOrder.getOrderId();

        } catch (RuntimeException e) {
            System.err.println("Błąd: " + e.getMessage());
            return "redirect:/cart?error=true";
        }
    }


    @GetMapping("/user/purchase/{id}")
    public String showPurchaseSummary(@PathVariable("id") Long orderId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            Order order = orderService.getOrderWithDetails(orderId);

            if (!order.getUserId().equals(user.getUserId())) {
                return "redirect:/user/profile";
            }

            model.addAttribute("order", order);
            model.addAttribute("pageTitle", "Podsumowanie zamówienia #" + orderId);

            return "purchase_summary";

        } catch (Exception e) {
            return "redirect:/user/profile";
        }
    }

    @GetMapping("/user/purchase/{id}/pdf")
    public ResponseEntity<byte[]> downloadInvoice(@PathVariable("id") Long orderId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return ResponseEntity.status(401).build();

        Order order = orderService.getOrderWithDetails(orderId);

        if (!order.getUserId().equals(user.getUserId())) {
            return ResponseEntity.status(403).build();
        }

        byte[] pdfBytes = pdfService.generateInvoicePdf(order);
        // ------------------------

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=faktura_" + orderId + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
    @Autowired
    private EmailService emailService;

    @GetMapping("/test-email")
    @ResponseBody
    public String testEmail() {
        try {

            emailService.sendEmailWithAttachment(
                    "janeksp18@gmail.com",
                    "Test maila",
                    "To jest test",
                    new byte[10], // pusty plik
                    "test.pdf"
            );
            return "Mail wysłany (sprawdź konsolę czy nie ma błędów)";
        } catch (Exception e) {
            return "Błąd: " + e.getMessage();
        }
    }

}
