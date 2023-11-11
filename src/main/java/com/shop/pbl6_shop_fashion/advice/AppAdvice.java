package com.shop.pbl6_shop_fashion.advice;

import com.shop.pbl6_shop_fashion.exception.DuplicateUsernameException;
import com.shop.pbl6_shop_fashion.exception.InvalidUserException;
import com.shop.pbl6_shop_fashion.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.InvalidClassException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class AppAdvice {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError(errors);
        URI uri = ServletUriComponentsBuilder.fromRequestUri(request).build().toUri();
        errorResponse.setPath(uri.getPath());

        return errorResponse;
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<String> handleException(InvalidUserException ex) {
        // Xử lý ngoại lệ ở đây
        String errorMessage = ex.getMessage();
        return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateUsernameException.class)
    public ErrorResponse handleDuplicateUsernameException(DuplicateUsernameException ex, HttpServletRequest request) {
        return getErrorResponse(request, ex.getMessage(), ex,HttpStatus.BAD_REQUEST.value());
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotException(UserNotFoundException ex, HttpServletRequest request) {
        return getErrorResponse(request, ex.getMessage(), ex,HttpStatus.NOT_FOUND.value());
    }
    private ErrorResponse getErrorResponse(HttpServletRequest request, String message, Exception ex,int status) {
        ErrorResponse errorResponse = new ErrorResponse();
        Map<String, String> errors = new HashMap<>();
        errors.put("message", message);
        errorResponse.setStatus(status);
        errorResponse.setError(errors);
        URI uri = ServletUriComponentsBuilder.fromRequestUri(request).build().toUri();
        errorResponse.setPath(uri.getPath());
        return errorResponse;
    }
}
