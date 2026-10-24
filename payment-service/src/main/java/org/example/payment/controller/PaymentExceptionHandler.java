package org.example.payment.controller;

import org.example.payment.dto.ErrorDto;
import org.example.payment.exception.MyPaymentException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Превращает ошибки платёжного ядра (в том числе пришедшие от сервиса продуктов)
 * в понятный HTTP-ответ с телом ErrorDto.
 */
@RestControllerAdvice
public class PaymentExceptionHandler {

    @ExceptionHandler(MyPaymentException.class)
    public ResponseEntity<ErrorDto> handlePaymentException(MyPaymentException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ErrorDto(e.getCode(), e.getMessage()));
    }
}
