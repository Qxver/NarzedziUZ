package org.store.narzedziuz;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.store.narzedziuz.entity.Order;
import org.store.narzedziuz.entity.Review;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.OrderService;
import org.store.narzedziuz.repository.UserRepository;
import org.store.narzedziuz.repository.ReviewRepository;
import org.store.narzedziuz.repository.CategoryRepository;
import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserRepository userRepository;
    private final OrderService orderService;
    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("user");

        if (sessionUser == null) {
            return "redirect:/login";
        }
        System.out.println(">>> /user/profile hit for: " + sessionUser.getEmail());

        User user = userRepository.findById(sessionUser.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found in DB"));


        System.out.println(">>> User found: " + user.getEmail());
        Long userId = user.getUserId();
        System.out.println(">>> UserId: " + userId);

        List<Order> orders = orderService.getUserOrders(userId);
        System.out.println(">>> Orders found: " + (orders != null ? orders.size() : "null"));


        List<Review> reviews = reviewRepository.findByUser(user);

        model.addAttribute("categories", categoryRepository.findAll());

        int totalPurchases = orders.size();
        BigDecimal totalSpent = orders.stream()
                .map(Order::getPriceSum)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalRatings = reviews.size();

        model.addAttribute("user", user);
        model.addAttribute("purchases", orders);
        model.addAttribute("ratings", reviews);

        model.addAttribute("totalPurchases", totalPurchases);
        model.addAttribute("totalSpent", totalSpent);
        model.addAttribute("totalRatings", totalRatings);

        model.addAttribute("pageTitle", "My Profile");
        model.addAttribute("welcomeMessage", "Welcome, " + user.getFirstName());
        model.addAttribute("purchaseHistoryTitle", "Purchase History");
        model.addAttribute("ratingsTitle", "Your Ratings");
        model.addAttribute("searchPlaceholder", "Szukaj produktów...");

        model.addAttribute("userConfig", Map.of(
                "background_color", "#f5f7fa",
                "surface_color", "#ffffff",
                "primary_action_color", "#0056b3",
                "text_color", "#1a202c"
        ));

        String initials = "";
        if (user.getFirstName() != null && !user.getFirstName().isEmpty()) {
            initials += user.getFirstName().charAt(0);
        }
        if (user.getLastName() != null && !user.getLastName().isEmpty()) {
            initials += user.getLastName().charAt(0);
        }
        model.addAttribute("initials", initials.toUpperCase());

        return "profil";
    }
}
