package org.store.narzedziuz.controller;

import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.UserService;
import org.store.narzedziuz.service.RecaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final RecaptchaService recaptchaService;

    @Value("${recaptcha.site-key}")
    private String recaptchaSiteKey;

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam(name = "g-recaptcha-response", required = false) String recaptchaResponse,
                        Model model,
                        HttpSession session) {
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        
        if (!recaptchaService.verifyRecaptcha(recaptchaResponse)) {
            model.addAttribute("error", "Please complete the reCAPTCHA verification");
            return "login";
        }
        
        try {
            User user = userService.loginUser(email, password);
            // Zmieniamy klucz na "loggedInUser", żeby pasował do UserProfileController
            // ALBO zmieniamy w UserProfileController na "user".
            // Zróbmy tak, żeby było spójnie wszędzie: "user"
            session.setAttribute("user", user);
            // Reszta atrybutów (userId, userName) jest ok
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getFirstName());
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session, Model model) {
        // If already logged in, redirect to home
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           @RequestParam(name = "g-recaptcha-response", required = false) String recaptchaResponse,
                           Model model,
                           HttpSession session) {
        model.addAttribute("recaptchaSiteKey", recaptchaSiteKey);
        
        if (!recaptchaService.verifyRecaptcha(recaptchaResponse)) {
            model.addAttribute("error", "Please complete the reCAPTCHA verification");
            return "register";
        }
        
        try {
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                return "register";
            }

            User user = userService.registerUser(email, password, firstName, lastName);
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getFirstName());
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }


}