package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundException(MethodArgumentNotValidException e) {
        String errorMessage = e.getMessage();
        log.error("Not Found Exception = {}", errorMessage);
        return new ErrorResponse("BAD_REQUEST",
                "Incorrectly made request.",
                e.getFieldError().getDefaultMessage() + " Value: " + e.getFieldError().getRejectedValue(),
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundException(MissingServletRequestParameterException e) {
        String errorMessage = e.getMessage();
        log.error("Not Found Exception = {}", errorMessage);
        return new ErrorResponse("BAD_REQUEST",
                "Incorrectly made request.",
                errorMessage,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateException(IllegalArgumentException e) {
        String errorMessage = e.getMessage();
        log.error("Illegal Argument Exception Exception = {}", errorMessage);
        return new ErrorResponse("BAD_REQUEST",
                "Incorrectly made request.",
                errorMessage,
                LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
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
                Arrays.toString(e.getStackTrace()),
                LocalDateTime.now());
    }
}
