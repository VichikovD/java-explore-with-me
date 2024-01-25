package ru.yandex.practicum.exception;

public class ErrorResponse {
    private final String error;
    private final String message;
    private String stackTrace;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(String error, String message, String stackTrace) {
        this.error = error;
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }
}
