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

        if (comment != null && !comment.trim().isEmpty()) {
            review.setComment(comment.trim());
        } else {
            review.setComment(null);
        }

        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(Long reviewId, Long userId, Integer rating, String comment) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Opinia nie znaleziona"));

        // Verify that the review belongs to the user
        if (!review.getUserId().equals(userId)) {
            throw new RuntimeException("Nie masz uprawnień do edycji tej opinii");
        }

        review.setRating(rating);

        if (comment != null && !comment.trim().isEmpty()) {
            review.setComment(comment.trim());
        } else {
            review.setComment(null);
        }

        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Opinia nie znaleziona"));

        if (!review.getUserId().equals(userId)) {
            throw new RuntimeException("Nie masz uprawnień do usunięcia tej opinii");
        }

        reviewRepository.delete(review);
    }

    public Review getUserReviewForProduct(Long userId, Long productId) {
        return reviewRepository.findByUserIdAndProductId(userId, productId);
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