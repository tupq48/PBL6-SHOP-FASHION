package com.shop.pbl6_shop_fashion.advice;

import com.shop.pbl6_shop_fashion.exception.*;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleException(InvalidUserException ex, HttpServletRequest request) {
        // Xử lý ngoại lệ ở đây
        String errorMessage = ex.getMessage();
        return getErrorResponse(request, ex.getMessage(), ex, HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DuplicateUsernameException.class)
    public ErrorResponse handleDuplicateUsernameException(DuplicateUsernameException ex, HttpServletRequest request) {
        return getErrorResponse(request, ex.getMessage(), ex, HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LockedOrDisableUserException.class)
    public ErrorResponse handleLockedOrDisableUserException(LockedOrDisableUserException ex, HttpServletRequest request) {
        return getErrorResponse(request, ex.getMessage(), ex, HttpStatus.UNAUTHORIZED.value());
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotException(UserNotFoundException ex, HttpServletRequest request) {
        return getErrorResponse(request, ex.getMessage(), ex, HttpStatus.NOT_FOUND.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ErrorResponse handleUserNotException(UniqueConstraintViolationException ex, HttpServletRequest request) {
        return getErrorResponse(request, ex.getMessage(), ex, HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OTPSetPasswordException.class)
    public ErrorResponse handleUserNotException(OTPSetPasswordException ex, HttpServletRequest request) {
        return getErrorResponse(request, ex.getMessage(), ex, HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordException.class)
    public ErrorResponse handleUserNotException(PasswordException ex, HttpServletRequest request) {
        return getErrorResponse(request, ex.getMessage(), ex, HttpStatus.BAD_REQUEST.value());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RoleException.class)
    public ErrorResponse handleUserNotException(RoleException ex, HttpServletRequest request) {
        return getErrorResponse(request, ex.getMessage(), ex, HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(JwtException.class)
    public ErrorResponse handleJwtException(JwtException ex, HttpServletRequest request) {
        ResponseStatus httpStatus =ex.getClass().getAnnotation(ResponseStatus.class);
        return getErrorResponse(request, ex.getMessage(), ex, httpStatus.value().value());
    }

    @ExceptionHandler(VoucherBaseException.class)
    public ErrorResponse handleVoucherException(VoucherBaseException ex,HttpServletRequest request){
        return getErrorResponse(request, ex.getMessage(), ex, ex.getStatusCode().value());
    }


    private ErrorResponse getErrorResponse(HttpServletRequest request, String message, Exception ex, int status) {
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
