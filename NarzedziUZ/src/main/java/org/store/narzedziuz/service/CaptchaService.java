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
            // Cap.js verification endpoint format
            String verifyUrl = captchaUrl + "/siteverify";
            
            System.out.println("Verifying captcha at: " + verifyUrl);
            System.out.println("Token: " + token);
            System.out.println("Secret: " + secret);

            Map<String, String> body = new HashMap<>();
            body.put("secret", secret);
            body.put("response", token);  // Cap.js expects 'response' field, not 'token'

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

            System.out.println("Request body: " + objectMapper.writeValueAsString(body));

            Map<String, Object> response = restTemplate.postForObject(verifyUrl, request, Map.class);
            
            System.out.println("Captcha verification response: " + response);

            if (response != null) {
                Boolean success = (Boolean) response.get("success");
                if (success != null && success) {
                    return true;
                } else {
                    System.err.println("Captcha verification failed. Response: " + response);
                    return false;
                }
            }
            
            System.err.println("Captcha verification returned null response");
            return false;
        } catch (Exception e) {
            System.err.println("Captcha verification error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}