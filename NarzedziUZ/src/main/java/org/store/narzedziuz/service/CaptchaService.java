package org.store.narzedziuz.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@Service
public class CaptchaService {

    @Value("${captcha.url}")
    private String captchaUrl;

    @Value("${captcha.site-key}")
    private String siteKey;

    @Value("${captcha.secret}")
    private String secret;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public boolean verifyCaptcha(String token) {
        try {
            String verifyUrl = captchaUrl + "/" + siteKey + "/siteverify";

            Map<String, String> body = new HashMap<>();
            body.put("secret", secret);
            body.put("response", token);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

            Map<String, Object> response = restTemplate.postForObject(verifyUrl, request, Map.class);

            return response != null && (boolean) response.getOrDefault("success", false);
        } catch (Exception e) {
            return false;
        }
    }
}