package com.dinidu.review_notification_service.controller;

import com.dinidu.review_notification_service.document.NotificationArchive;
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

    // ---------------- REVIEW CRUD ----------------

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Review> createReview(
            @RequestPart("review") Review review,
            @RequestPart(value = "files", required = false) List<MultipartFile> files) throws IOException {

        return ResponseEntity.ok(
                reviewService.createReview(review, files));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews() {
        return ResponseEntity.ok(
                reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReview(
            @PathVariable String id) {

        return ResponseEntity.ok(
                reviewService.getReview(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(
            @PathVariable String id,
            @RequestBody Review review) {

        return ResponseEntity.ok(
                reviewService.updateReview(id, review));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String id) {

        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
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

    // ---------------- NOTIFICATION CRUD ----------------

    @PostMapping("/notifications")
    public ResponseEntity<NotificationArchive> createNotification(
            @RequestBody NotificationArchive notification) {

        return ResponseEntity.ok(
                reviewService.createNotification(notification));
    }

    @GetMapping("/notifications")
    public ResponseEntity<List<NotificationArchive>> getNotifications() {
        return ResponseEntity.ok(
                reviewService.getAllNotifications());
    }

    @GetMapping("/notifications/{id}")
    public ResponseEntity<NotificationArchive> getNotification(
            @PathVariable String id) {

        return ResponseEntity.ok(
                reviewService.getNotification(id));
    }

    @PutMapping("/notifications/{id}")
    public ResponseEntity<NotificationArchive> updateNotification(
            @PathVariable String id,
            @RequestBody NotificationArchive notification) {

        return ResponseEntity.ok(
                reviewService.updateNotification(id, notification));
    }

    @DeleteMapping("/notifications/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable String id) {

        reviewService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/notifications/user/{userId}")
    public ResponseEntity<List<NotificationArchive>> getNotificationsByUser(@PathVariable Long userId) {

        return ResponseEntity.ok(
                reviewService.getNotificationsByUser(userId));
    }
}