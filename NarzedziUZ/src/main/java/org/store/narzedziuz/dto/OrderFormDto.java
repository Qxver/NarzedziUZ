package org.store.narzedziuz.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderFormDto {
    // Dane adresowe (shipping)
    private String email;
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String houseNumber;

    private BigDecimal finalPrice;

    // Metoda płatności
    private String paymentMethod; // BLIK, CARD, TRANSFER, PAYPAL

    // Dane płatności (opcjonalne, zależne od metody)
    private String blikCode;
    private String cardNumber;
    private String cardExpiry;
    private String cardCvc;

    public BigDecimal getFinalPrice() { return finalPrice; }
    public void setFinalPrice(BigDecimal finalPrice) { this.finalPrice = finalPrice; }
}
