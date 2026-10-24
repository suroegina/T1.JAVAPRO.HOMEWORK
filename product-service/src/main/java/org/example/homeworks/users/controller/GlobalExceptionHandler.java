package org.example.homeworks.users.controller;

import org.example.homeworks.users.dto.ErrorDto;
import org.example.homeworks.users.exception.ProductException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Превращает доменные исключения сервиса продуктов в понятный HTTP-ответ
 * с телом ErrorDto (code + message).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductException.class)
    public ResponseEntity<ErrorDto> handleProductException(ProductException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ErrorDto(e.getCode(), e.getMessage()));
    }
}
