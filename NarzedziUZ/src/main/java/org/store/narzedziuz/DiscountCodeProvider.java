package org.store.narzedziuz;

import org.springframework.stereotype.Component;
import org.store.narzedziuz.entity.DiscountCode;

import java.util.List;
import java.util.Optional;

@Component
public class DiscountCodeProvider {

    private final List<DiscountCode> codes = List.of(
            new DiscountCode("git jest git", 50),
            new DiscountCode("narzedziuz26", 15),
            new DiscountCode("pieniadzezalas", 10)
    );

    public Optional<DiscountCode> findByCode(String code) {
        return codes.stream()
                .filter(c -> c.getCode().equalsIgnoreCase(code))
                .findFirst();
    }
}

