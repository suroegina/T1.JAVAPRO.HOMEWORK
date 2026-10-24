package org.example.payment.dto;

import java.math.BigDecimal;

/**
 * Запрос на исполнение платежа: клиент выбирает продукт и указывает сумму.
 */
public record PaymentRequestDto(Long userId, Long productId, BigDecimal amount) {
}
