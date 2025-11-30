package org.store.narzedziuz;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.UserService;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;


    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users/list";
    }

    @GetMapping("/search")
    public String searchUsers(@RequestParam String query, Model model) {
        List<User> users;
        if (query != null && !query.trim().isEmpty()) {
            users = userService.getUsersByEmail(query);
        } else {
            users = userService.getAllUsers();
        }
        model.addAttribute("users", users);
        return "admin/users/list";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
        } catch (RuntimeException e) {
            System.err.println("Błąd podczas usuwania użytkownika: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Test działa!" ;
    }
}
