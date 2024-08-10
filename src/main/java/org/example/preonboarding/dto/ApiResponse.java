package org.example.preonboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private final String status;
    private final String code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success(String code, T data) {
        return new ApiResponse<>("OK", code, "요청이 성공적으로 처리되었습니다.", data);
    }

    public static <T> ApiResponse<T> error(String code, String message, T data) {
        return new ApiResponse<>("ERROR", code, message, data);
    }

}
