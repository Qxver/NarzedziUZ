package org.store.narzedziuz.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiscountCode {
    private String code;
    private int percent;
}

