package org.example.payment.exception;

/**
 * Доменное исключение платёжного ядра. Несёт HTTP-статус и семантический код.
 */
public class MyPaymentException extends RuntimeException {

    private final int status;
    private final String code;

    public MyPaymentException(int status, String code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
