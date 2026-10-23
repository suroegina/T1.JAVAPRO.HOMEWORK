package org.example.homeworks.users.dto;

import java.math.BigDecimal;

public record ProductDto(Long id, String username, BigDecimal balance, String productType, Long userId) {}

