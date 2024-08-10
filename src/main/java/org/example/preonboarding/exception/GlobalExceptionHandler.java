package org.example.preonboarding.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.preonboarding.dto.ApiResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
        log.info("[handleValidationException]");
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : exception.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ApiResponse.error(ErrorCode.VALIDATION_ERROR.name(), "validation failed", errors);
    }

    // 커스텀 예외 처리
    @ExceptionHandler(CustomException.class)
    public ApiResponse<?> handleCustomException(CustomException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ApiResponse.error(errorCode.name(), errorCode.getErrorMessage(), null);
    }

    // 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleGenericException(Exception exception) {
        return ApiResponse.error("INTERNAL_SERVER_ERROR", exception.getMessage(), null);
    }

}
