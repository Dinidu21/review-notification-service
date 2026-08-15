package com.dinidu.review_notification_service.config;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirestoreConfig {

    @Bean
    public Firestore firestore(
            @Value("${spring.cloud.gcp.firestore.project-id}") String projectId) {

        return FirestoreOptions
                .getDefaultInstance()
                .toBuilder()
                .setProjectId(projectId)
                .build()
                .getService();
    }
}