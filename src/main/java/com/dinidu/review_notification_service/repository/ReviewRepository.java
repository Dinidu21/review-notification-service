package com.dinidu.review_notification_service.repository;

import com.dinidu.review_notification_service.document.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {

    List<Review> findByEventId(Long eventId);

    List<Review> findByUserId(Long userId);
}