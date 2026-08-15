package com.dinidu.review_notification_service.service;

import com.dinidu.review_notification_service.document.NotificationArchive;
import com.dinidu.review_notification_service.document.Review;
import com.dinidu.review_notification_service.repository.NotificationArchiveRepository;
import com.dinidu.review_notification_service.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final NotificationArchiveRepository notificationArchiveRepository;
    private final CloudStorageService cloudStorageService;

    public ReviewService(
            ReviewRepository reviewRepository,
            NotificationArchiveRepository notificationArchiveRepository,
            CloudStorageService cloudStorageService) {

        this.reviewRepository = reviewRepository;
        this.notificationArchiveRepository = notificationArchiveRepository;
        this.cloudStorageService = cloudStorageService;
    }

    // ---------------- REVIEW CRUD ----------------

    public Review createReview(
            Review review,
            List<MultipartFile> files) throws IOException {

        Review savedReview = reviewRepository.save(review);

        if (files != null && !files.isEmpty()) {
            List<String> mediaUrls = new ArrayList<>();

            for (MultipartFile file : files) {
                if (file == null || file.isEmpty()) {
                    continue;
                }

                String mediaUrl = cloudStorageService.uploadReviewMedia(
                        file,
                        savedReview.getId());

                mediaUrls.add(mediaUrl);
            }

            savedReview.setMediaUrls(mediaUrls);
            savedReview = reviewRepository.save(savedReview);
        }

        return savedReview;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReview(String id) {
        return reviewRepository.findById(id);
    }

    public Review updateReview(String id, Review request) {
        Review review = reviewRepository.findById(id);

        review.setEventId(request.getEventId());
        review.setUserId(request.getUserId());
        review.setRating(request.getRating());
        review.setText(request.getText());

        if (request.getMediaUrls() != null) {
            review.setMediaUrls(request.getMediaUrls());
        }

        return reviewRepository.save(review);
    }

    public void deleteReview(String id) {
        reviewRepository.deleteById(id);
    }

    public List<Review> getByEvent(Long eventId) {
        return reviewRepository.findByEventId(eventId);
    }

    public List<Review> getByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    // ---------------- NOTIFICATION CRUD ----------------

    public NotificationArchive createNotification(
            NotificationArchive notification) {

        return notificationArchiveRepository.save(notification);
    }

    public List<NotificationArchive> getAllNotifications() {
        return notificationArchiveRepository.findAll();
    }

    public NotificationArchive getNotification(String id) {
        return notificationArchiveRepository.findById(id);
    }

    public NotificationArchive updateNotification(
            String id,
            NotificationArchive request) {

        NotificationArchive notification = notificationArchiveRepository.findById(id);

        notification.setUserId(request.getUserId());
        notification.setType(request.getType());
        notification.setPayload(request.getPayload());

        return notificationArchiveRepository.save(notification);
    }

    public void deleteNotification(String id) {
        notificationArchiveRepository.deleteById(id);
    }

    public List<NotificationArchive> getNotificationsByUser(
            Long userId) {

        return notificationArchiveRepository.findByUserId(userId);
    }
}