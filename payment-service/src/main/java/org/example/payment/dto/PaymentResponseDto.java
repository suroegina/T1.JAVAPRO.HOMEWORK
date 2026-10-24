package org.example.payment.dto;

import java.math.BigDecimal;

/**
 * Результат исполнения платежа.
 */
public record PaymentResponseDto(
        String status,
        Long productId,
        BigDecimal amount,
        BigDecimal newBalance,
        String message
) {
}
