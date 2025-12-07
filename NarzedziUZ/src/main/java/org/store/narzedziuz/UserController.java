package org.store.narzedziuz;


import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.UserService;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/")
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public String searchUsers(@RequestParam(value = "email", required = false) String email, Model model) {
        List<User> users;
        if (email != null && !email.isBlank()) {
            users = userService.searchUsersByEmail(email);
            model.addAttribute("searchTerm", email);
        } else {
            users = userService.findAllUsers();
        }

        model.addAttribute("users", users);
        return "admin/list";
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
