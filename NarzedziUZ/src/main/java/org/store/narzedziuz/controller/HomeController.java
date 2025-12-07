package org.store.narzedziuz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.store.narzedziuz.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;

    @RequestMapping("/")
    public String index(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", session.getAttribute("userName"));
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }

        model.addAttribute("products", productService.getAllProducts());

        return "home";
    }
}