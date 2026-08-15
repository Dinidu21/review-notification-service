package com.dinidu.review_notification_service.controller;

import com.dinidu.review_notification_service.document.Review;
import com.dinidu.review_notification_service.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Review> postReview(
            @RequestPart("review") Review review,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        Review savedReview = reviewService.createReview(review, files);

        return ResponseEntity.ok(savedReview);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Review>> getByEvent(
            @PathVariable Long eventId) {

        return ResponseEntity.ok(
                reviewService.getByEvent(eventId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Review>> getByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                reviewService.getByUser(userId));
    }
}