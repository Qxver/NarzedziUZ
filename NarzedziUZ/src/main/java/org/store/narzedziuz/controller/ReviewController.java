package org.store.narzedziuz.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.service.ReviewService;

@Controller
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/add/{productId}")
    public String addReview(
            @PathVariable Long productId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You need to be logged in to add a review.");
            return "redirect:/login";
        }

        try {
            reviewService.addReview(user.getUserId(), productId, rating, comment);
            redirectAttributes.addFlashAttribute("success", "Review added successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
        }

        return "redirect:/product/" + productId;
    }

    @PostMapping("/update/{reviewId}")
    public String updateReview(
            @PathVariable Long reviewId,
            @RequestParam Long productId,
            @RequestParam Integer rating,
            @RequestParam String comment,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You need to be logged in to edit a review.");
            return "redirect:/login";
        }

        try {
            reviewService.updateReview(reviewId, user.getUserId(), rating, comment);
            redirectAttributes.addFlashAttribute("success", "Review updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
        }

        return "redirect:/product/" + productId;
    }

    @PostMapping("/delete/{reviewId}")
    public String deleteReview(
            @PathVariable Long reviewId,
            @RequestParam Long productId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You need to be logged in to delete a review.");
            return "redirect:/login";
        }

        try {
            reviewService.deleteReview(reviewId, user.getUserId());
            redirectAttributes.addFlashAttribute("success", "Review deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred: " + e.getMessage());
        }

        return "redirect:/product/" + productId;
    }
}