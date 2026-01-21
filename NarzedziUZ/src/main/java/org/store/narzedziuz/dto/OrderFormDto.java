package org.store.narzedziuz.dto;

import lombok.Data;

@Data
public class OrderFormDto {
    // Dane adresowe (shipping)
    private String email;
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String houseNumber;

    // Metoda płatności
    private String paymentMethod; // BLIK, CARD, TRANSFER, PAYPAL

    // Dane płatności (opcjonalne, zależne od metody)
    private String blikCode;
    private String cardNumber;
    private String cardExpiry;
    private String cardCvc;
}
