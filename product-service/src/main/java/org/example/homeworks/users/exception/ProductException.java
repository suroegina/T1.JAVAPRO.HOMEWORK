package org.example.homeworks.users.exception;

import org.springframework.http.HttpStatus;

/**
 * Доменное исключение сервиса продуктов. Несёт HTTP-статус и семантический
 * код ошибки, чтобы клиент (в том числе платёжное ядро) получал понятный ответ.
 */
public class ProductException extends RuntimeException {

    private final HttpStatus status;
    private final String code;

    public ProductException(HttpStatus status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
