package org.store.narzedziuz.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class RecaptchaService {

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @Value("${recaptcha.secret-key}")
    private String secretKey;

    @Value("${recaptcha.enabled:true}")
    private boolean recaptchaEnabled;

    public boolean verifyRecaptcha(String recaptchaResponse) {
        if (!recaptchaEnabled) {
            return true; // Skip verification if disabled
        }

        if (recaptchaResponse == null || recaptchaResponse.isEmpty()) {
            return false;
        }

        try {
            RestTemplate restTemplate = new RestTemplate();
            
            String url = RECAPTCHA_VERIFY_URL + "?secret=" + secretKey + "&response=" + recaptchaResponse;
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, null, HashMap.class);
            
            if (response != null && response.containsKey("success")) {
                return (Boolean) response.get("success");
            }
            
            return false;
        } catch (Exception e) {
            // Log the error in production
            return false;
        }
    }
}
