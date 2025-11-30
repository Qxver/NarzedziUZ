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
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public String listUsers(HttpSession session, Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/users/list";
    }

    @GetMapping("/search")
    public String searchUsers(HttpSession session, @RequestParam String query, Model model) {
        return "admin/users/list";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(HttpSession session, @PathVariable Long id) {
        return "redirect:/admin/users";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(HttpSession session) {
        return "Test działa!";
    }
}
