package org.store.narzedziuz;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String index(HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user != null) {
            model.addAttribute("userName", session.getAttribute("userName"));
            model.addAttribute("isLoggedIn", true);
        } else {
            model.addAttribute("isLoggedIn", false);
        }
        return "home";
    }
}