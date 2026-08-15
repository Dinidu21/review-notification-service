package com.dinidu.review_notification_service.repository;

import com.dinidu.review_notification_service.document.Review;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Repository
public class ReviewRepository {

    private static final String COLLECTION = "reviews";

    private final Firestore firestore;

    public ReviewRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    public Review save(Review review) {
        try {
            DocumentReference document;

            if (review.getId() == null || review.getId().isBlank()) {
                document = firestore.collection(COLLECTION).document();
                review.setId(document.getId());
            } else {
                document = firestore
                        .collection(COLLECTION)
                        .document(review.getId());
            }

            document.set(review).get();

            return review;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while saving review", e);

        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to save review", e);
        }
    }

    public List<Review> findAll() {
        try {
            QuerySnapshot snapshot = firestore
                    .collection(COLLECTION)
                    .get()
                    .get();

            List<Review> reviews = new ArrayList<>();

            for (QueryDocumentSnapshot document : snapshot.getDocuments()) {
                Review review = document.toObject(Review.class);
                review.setId(document.getId());
                reviews.add(review);
            }

            return reviews;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while reading reviews", e);

        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to read reviews", e);
        }
    }

    public Review findById(String id) {
        try {
            DocumentSnapshot document = firestore
                    .collection(COLLECTION)
                    .document(id)
                    .get()
                    .get();

            if (!document.exists()) {
                throw new IllegalArgumentException("Review not found");
            }

            Review review = document.toObject(Review.class);
            review.setId(document.getId());

            return review;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while reading review", e);

        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to read review", e);
        }
    }

    public void deleteById(String id) {
        try {
            DocumentSnapshot document = firestore
                    .collection(COLLECTION)
                    .document(id)
                    .get()
                    .get();

            if (!document.exists()) {
                throw new IllegalArgumentException("Review not found");
            }

            firestore
                    .collection(COLLECTION)
                    .document(id)
                    .delete()
                    .get();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while deleting review", e);

        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to delete review", e);
        }
    }

    public List<Review> findByEventId(Long eventId) {
        try {
            ApiFuture<QuerySnapshot> future = firestore
                    .collection(COLLECTION)
                    .whereEqualTo("eventId", eventId)
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            List<Review> reviews = new ArrayList<>();

            for (QueryDocumentSnapshot document : documents) {
                Review review = document.toObject(Review.class);
                review.setId(document.getId());
                reviews.add(review);
            }

            return reviews;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while reading reviews", e);

        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to read reviews by eventId", e);
        }
    }

    public List<Review> findByUserId(Long userId) {
        try {
            ApiFuture<QuerySnapshot> future = firestore
                    .collection(COLLECTION)
                    .whereEqualTo("userId", userId)
                    .get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            List<Review> reviews = new ArrayList<>();

            for (QueryDocumentSnapshot document : documents) {
                Review review = document.toObject(Review.class);
                review.setId(document.getId());
                reviews.add(review);
            }

            return reviews;

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted while reading reviews", e);

        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to read reviews by userId", e);
        }
    }
}