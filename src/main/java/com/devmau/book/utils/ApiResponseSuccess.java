package com.devmau.book.utils;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseSuccess<T> {

    private int status;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    private boolean success = true;

    private T data;

    private String message;

}
