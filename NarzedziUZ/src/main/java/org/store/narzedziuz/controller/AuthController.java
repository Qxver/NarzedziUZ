package org.store.narzedziuz.controller;

import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.UserService;
import org.store.narzedziuz.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final CaptchaService captchaService;

    @GetMapping("/login")
    public String loginPage(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        @RequestParam(required = false) String captchaToken,
                        Model model,
                        HttpSession session) {
        try {
            // Captcha is optional - verify only if token is provided
            if (captchaToken != null && !captchaToken.isEmpty()) {
                captchaService.verifyCaptcha(captchaToken);
            }

            User user = userService.loginUser(email, password);
            // Store user in session
            session.setAttribute("user", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userName", user.getFirstName());
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "login";
        }
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        // If already logged in, redirect to home
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String confirmPassword,
                           @RequestParam(required = false) String captchaToken,
                           Model model,
                           HttpSession session) {
        try {
            // Captcha is optional - verify only if token is provided
            if (captchaToken != null && !captchaToken.isEmpty()) {
                captchaService.verifyCaptcha(captchaToken);
            }

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

    @GetMapping("/profile")
    public String profilePage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "profile";
    }
}