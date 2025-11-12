package org.store.narzedziuz;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import database.NarzedziUZDB;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    @PostMapping("/register")
    public String handleRegistration(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String confirmPassword) throws SQLException {
        if (!password.equals(confirmPassword)) {
            // Tu możesz dodać logikę informowania użytkownika o błędzie, np. przekazanie info do widoku
            return "register";
        }
        List emails = new ArrayList();
        emails = NarzedziUZDB.selectSmth("user","email",null,null);
        for (Object email : emails) {
            if (email.toString().equals(username)) {
                return "register";
            }
        }
        NarzedziUZDB.insertUser(null,username,password,firstName,lastName,"Użytkownik");
        // Tu powinna być logika zapisu użytkownika do bazy danych, np. wywołanie serwisu:
        // userService.registerNewUser(firstName, lastName, username, password);

        return "redirect:/login"; // po pomyślnej rejestracji przekierowanie do logowania
    }
}
