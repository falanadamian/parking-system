package com.debosz.kamil.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {

    private T data;
    private String message;

    public ResponseDTO(String message, T data) {
        this.data = data;
        this.message = message;
    }

    public static <E> ResponseDTO<E> of(String message, E data) {
        return new ResponseDTO<>(message, data);
    }

}

