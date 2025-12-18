package org.store.narzedziuz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.store.narzedziuz.entity.Product;
import org.store.narzedziuz.entity.Review;
import org.store.narzedziuz.entity.User;
import org.store.narzedziuz.repository.ProductRepository;
import org.store.narzedziuz.repository.ReviewRepository;
import org.store.narzedziuz.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Review addReview(Long userId, Long productId, Integer rating, String comment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie znaleziony"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produkt nie znaleziony"));

        boolean hasReview = reviewRepository.existsByUserIdAndProductId(userId, productId);
        if (hasReview) {
            throw new RuntimeException("Już dodałeś opinię do tego produktu");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(comment.trim());

        return reviewRepository.save(review);
    }

    public List<Review> getProductReviews(Long productId) {
        return reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
    }

    public Double getAverageRating(Long productId) {
        return reviewRepository.getAverageRatingByProductId(productId);
    }

    public long getReviewCount(Long productId) {
        return reviewRepository.countByProductId(productId);
    }
}