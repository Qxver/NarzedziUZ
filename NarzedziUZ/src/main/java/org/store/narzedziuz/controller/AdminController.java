package org.store.narzedziuz.controller;

import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.UserService;
import org.store.narzedziuz.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final ProductService productService;

    @GetMapping
    public String adminPanel(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        model.addAttribute("userName", user.getFirstName());
        return "admin/admin-panel";
    }

    @GetMapping("/users")
    public String manageUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        model.addAttribute("users", userService.getAllUsers());
        return "admin/manage-users";
    }

    @GetMapping("/products")
    public String manageProducts(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        model.addAttribute("products", productService.getAllProducts());
        return "admin/manage-products";
    }

    @PostMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        
        if (user == null || !"ADMIN".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        productService.deleteProduct(id);
        return "redirect:/admin/products?deleted";
    }
}