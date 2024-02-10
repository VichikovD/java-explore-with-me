package ru.yandex.practicum.exception;

import java.time.LocalDateTime;


public class NotFoundException extends RuntimeException {
    final String status = "NOT_FOUND";
    final String reason = "Required object was not found.";
    final LocalDateTime timestamp = LocalDateTime.now();
    final String message;

    public NotFoundException(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getReason() {
        return reason;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
