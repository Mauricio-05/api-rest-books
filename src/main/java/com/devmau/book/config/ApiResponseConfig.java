package com.devmau.book.config;

import com.devmau.book.utils.ApiResponseError;
import com.devmau.book.utils.ApiResponseSuccess;

public class ApiResponseConfig<T> {
    public static <T> ApiResponseSuccess<T> success(int status, T data, String message){
        return ApiResponseSuccess.<T>builder()
                .status(status)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponseError<T> error(int status, T error, String message){
        return ApiResponseError.<T>builder()
                .status(status)
                .error(error)
                .message(message)
                .build();
    }
}
