package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        String errorMessage = e.getMessage();
        log.error("Not Found Exception = {}", errorMessage);
        return new ErrorResponse(e.getStatus(),
                e.getReason(),
                e.getMessage(),
                e.getTimestamp());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotFoundException(DataIntegrityViolationException e) {
        String errorMessage = e.getMessage();
        log.error("Data Integrity Violation Exception = {}", errorMessage);
        return new ErrorResponse("CONFLICT",
                "Integrity constraint has been violated.",
                e.getMessage(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUndefinedException(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String errorMessage = e.getMessage();
        log.error("Exception = {}", errorMessage, e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "Unhandled server exception. Please report this error with exact timing to support",
                e.getMessage(),
                LocalDateTime.now());
    }
}