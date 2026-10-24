package org.example.payment.service;

import org.example.payment.client.ProductServiceClient;
import org.example.payment.dto.PaymentRequestDto;
import org.example.payment.dto.PaymentResponseDto;
import org.example.payment.dto.ProductDto;
import org.example.payment.exception.MyPaymentException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final ProductServiceClient productServiceClient;

    public PaymentService(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }

    /**
     * Возвращает продукты клиента, проксируя запрос в сервис продуктов.
     */
    public List<ProductDto> getProductsByUserId(Long userId) {
        return productServiceClient.getProductsByUserId(userId);
    }

    /**
     * Процесс исполнения платежа:
     * 1) выбор продукта (productId);
     * 2) проверка существования продукта (запрос в сервис продуктов);
     * 3) проверка достаточности средств;
     * 4) списание средств и возврат результата.
     */
    public PaymentResponseDto executePayment(PaymentRequestDto request) {
        // 2. Проверка существования: если продукта нет, сервис продуктов вернёт 404,
        //    и клиент получит ошибку PRODUCT_NOT_FOUND (через ProductServiceErrorHandler).
        ProductDto product = productServiceClient.getProductById(request.productId());

        // Продукт должен принадлежать указанному пользователю.
        if (!product.userId().equals(request.userId())) {
            throw new MyPaymentException(400, "PRODUCT_OWNER_MISMATCH",
                    "Product " + request.productId() + " does not belong to user " + request.userId());
        }

        // 3. Проверка достаточности средств.
        if (product.balance().compareTo(request.amount()) < 0) {
            throw new MyPaymentException(400, "INSUFFICIENT_FUNDS",
                    "Insufficient funds on product " + request.productId()
                            + ": balance=" + product.balance() + ", amount=" + request.amount());
        }

        // 4. Исполнение платежа — списание в сервисе продуктов.
        ProductDto updated = productServiceClient.debit(request.productId(), request.amount());

        return new PaymentResponseDto(
                "SUCCESS",
                request.productId(),
                request.amount(),
                updated.balance(),
                "Payment executed successfully");
    }
}
