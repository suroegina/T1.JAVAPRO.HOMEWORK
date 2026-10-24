package org.example.payment.dto;

/**
 * Унифицированное тело ошибки, которое платёжное ядро возвращает клиенту.
 */
public record ErrorDto(String code, String message) {
}
