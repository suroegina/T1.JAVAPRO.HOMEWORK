package org.example.homeworks.users.dto;

/**
 * Унифицированное тело ошибки, которое сервис продуктов возвращает клиенту.
 */
public record ErrorDto(String code, String message) {
}
