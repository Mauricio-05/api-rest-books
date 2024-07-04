package com.devmau.book.utils;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseError<T> {

    private int status;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    private boolean success = false;

    private T error;
    private String message;

}
