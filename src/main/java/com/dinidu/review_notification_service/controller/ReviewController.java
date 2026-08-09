package com.dinidu.review_notification_service.controller;

import com.dinidu.review_notification_service.document.Review;
import com.dinidu.review_notification_service.repository.ReviewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;

    public ReviewController(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @PostMapping
    public ResponseEntity<Review> postReview(@RequestBody Review review) {
        return ResponseEntity.ok(reviewRepository.save(review));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getByEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(reviewRepository.findByEventId(eventId));
    }
}