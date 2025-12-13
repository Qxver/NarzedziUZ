package org.store.narzedziuz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.store.narzedziuz.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final ProductService productService;

    @RequestMapping("/")
    public String index(HttpSession session, Model model,
                        @RequestParam(value = "sort", required = false) String sort) {

        Object user = session.getAttribute("user");
        model.addAttribute("isLoggedIn", user != null);
        if (user != null) {
            model.addAttribute("userName", session.getAttribute("userName"));
        }

        model.addAttribute("products", productService.getAllProductsSorted(sort));

        model.addAttribute("currentSort", sort);

        return "home";
    }

}