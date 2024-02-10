package ru.yandex.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.PrintWriter;
import java.io.StringWriter;

@Slf4j
@RestControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateException(ValidateException e) {
        String message = e.getMessage();
        log.error("Validate Exception = {}", message);
        return new ErrorResponse("ValidateException", message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = e.getMessage();
        log.error("MethodArgumentNotValidException = {}", message);
        return new ErrorResponse("MethodArgumentNotValidException", message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundException(MethodArgumentTypeMismatchException e) {
        String message = e.getMessage();
        log.error("MethodArgumentTypeMismatchException = {}", message);
        return new ErrorResponse("MethodArgumentTypeMismatchException", message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotFoundException(MissingServletRequestParameterException e) {
        String message = e.getMessage();
        log.error("MissingServletRequestParameterException = {}", message);
        return new ErrorResponse("MissingServletRequestParameterException", message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidateException(IllegalArgumentException e) {
        String message = e.getMessage();
        log.error("IllegalArgumentException = {}", message);
        return new ErrorResponse("IllegalArgumentException", message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleNotFoundException(DataIntegrityViolationException e) {
        String message = e.getMessage();
        log.error("DataIntegrityViolationException = {}", message);
        return new ErrorResponse("DataIntegrityViolationException", message);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        String errorMessage = e.getMessage();
        log.error("Exception = {}", errorMessage, e);
        return new ErrorResponse(e.toString(), errorMessage);
    }
}
