package com.dinidu.review_notification_service.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class CloudStorageService {

    private final Storage storage;
    private final String bucketName;

    public CloudStorageService(
            Storage storage,
            @Value("${app.gcs.bucket-name}") String bucketName) {

        this.storage = storage;
        this.bucketName = bucketName;
    }

    public String uploadReviewMedia(
            MultipartFile file,
            String reviewId) throws IOException {

        String originalFilename = file.getOriginalFilename();

        String filename = originalFilename != null
                ? originalFilename
                : "file";

        String objectName = "reviews/"
                + reviewId
                + "/"
                + UUID.randomUUID()
                + "-"
                + filename;

        BlobId blobId = BlobId.of(bucketName, objectName);

        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(
                blobInfo,
                file.getBytes());

        return "https://storage.googleapis.com/"
                + bucketName
                + "/"
                + objectName;
    }
}