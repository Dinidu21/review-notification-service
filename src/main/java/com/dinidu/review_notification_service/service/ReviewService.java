package com.dinidu.review_notification_service.service;

import com.dinidu.review_notification_service.document.Review;
import com.dinidu.review_notification_service.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CloudStorageService cloudStorageService;

    public ReviewService(
            ReviewRepository reviewRepository,
            CloudStorageService cloudStorageService) {

        this.reviewRepository = reviewRepository;
        this.cloudStorageService = cloudStorageService;
    }

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

    public List<Review> getByEvent(Long eventId) {
        return reviewRepository.findByEventId(eventId);
    }

    public List<Review> getByUser(Long userId) {
        return reviewRepository.findByUserId(userId);
    }
}