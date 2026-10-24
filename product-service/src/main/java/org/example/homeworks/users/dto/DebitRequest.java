package org.example.homeworks.users.dto;

import java.math.BigDecimal;

/**
 * Тело запроса на списание средств с продукта.
 */
public record DebitRequest(BigDecimal amount) {
}
