package org.example.payment.dto;

import java.math.BigDecimal;

/**
 * Копия DTO сервиса продуктов. Имена полей совпадают с JSON, который отдаёт
 * product-service. Внимание: поле "username" на самом деле содержит номер счёта
 * (так сериализует сервис продуктов).
 */
public record ProductDto(Long id, String username, BigDecimal balance, String productType, Long userId) {
}
