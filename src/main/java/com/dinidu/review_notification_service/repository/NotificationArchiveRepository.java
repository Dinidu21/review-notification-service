package com.dinidu.review_notification_service.repository;

import com.dinidu.review_notification_service.document.NotificationArchive;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class NotificationArchiveRepository {

    private static final String COLLECTION = "notifications_archive";

    private final Firestore firestore;

    public NotificationArchiveRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public NotificationArchive save(NotificationArchive notification) {

        try {
            DocumentReference document;

            if (notification.getId() == null || notification.getId().isBlank()) {
                document = firestore.collection(COLLECTION).document();
                notification.setId(document.getId());
            } else {
                document = firestore
                        .collection(COLLECTION)
                        .document(notification.getId());
            }

            document.set(notification).get();

            return notification;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(
                    "Interrupted while saving notification archive",
                    e);

        } catch (ExecutionException e) {
            throw new RuntimeException(
                    "Failed to save notification archive",
                    e);
        }
    }

    public List<NotificationArchive> findByUserId(Long userId) {

        try {
            ApiFuture<QuerySnapshot> future = firestore
                    .collection(COLLECTION)
                    .whereEqualTo("userId", userId)
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            List<NotificationArchive> notifications = new ArrayList<>();

            for (QueryDocumentSnapshot document : documents) {

                NotificationArchive notification = document.toObject(NotificationArchive.class);

                notification.setId(document.getId());

                notifications.add(notification);
            }

            return notifications;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(
                    "Interrupted while reading notification archive",
                    e);

        } catch (ExecutionException e) {
            throw new RuntimeException(
                    "Failed to read notification archive",
                    e);
        }
    }
}