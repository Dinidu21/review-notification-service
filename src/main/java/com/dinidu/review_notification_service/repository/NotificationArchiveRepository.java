package com.dinidu.review_notification_service.repository;

import com.dinidu.review_notification_service.document.NotificationArchive;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface NotificationArchiveRepository extends MongoRepository<NotificationArchive, String> {
    List<NotificationArchive> findByUserId(Long userId);
}