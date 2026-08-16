package com.dinidu.review_notification_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // Not-found lookups: findById on reviews/notifications
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map<String, Object>> handleNotFound(IllegalArgumentException ex) {
                return build(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        // Invalid JSON request body / malformed multipart "review" part
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Map<String, Object>> handleInvalidRequest(HttpMessageNotReadableException ex) {
                return build(HttpStatus.BAD_REQUEST, "Invalid request body");
        }

        // Bean validation failures
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
                String message = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .findFirst()
                                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                                .orElse("Validation failed");
                return build(HttpStatus.BAD_REQUEST, message);
        }

        // Genuine Firestore/GCS infrastructure failures — wrapped RuntimeException
        // from ReviewRepository/NotificationArchiveRepository on ExecutionException.
        // 500 is the correct status here; this just ensures a clean JSON body
        // instead of the whitelabel error page.
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, Object>> handleUnexpected(RuntimeException ex) {
                return build(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        }

        private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
                Map<String, Object> body = new LinkedHashMap<>();
                body.put("timestamp", Instant.now().toString());
                body.put("status", status.value());
                body.put("error", status.getReasonPhrase());
                body.put("message", message);
                return ResponseEntity.status(status).body(body);
        }
}