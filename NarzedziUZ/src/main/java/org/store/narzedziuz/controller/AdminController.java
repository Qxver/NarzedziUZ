package org.store.narzedziuz.controller;

import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

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
}