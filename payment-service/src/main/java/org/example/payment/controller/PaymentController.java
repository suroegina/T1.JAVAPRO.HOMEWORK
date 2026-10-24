package org.example.payment.controller;

import org.example.payment.dto.PaymentRequestDto;
import org.example.payment.dto.PaymentResponseDto;
import org.example.payment.dto.ProductDto;
import org.example.payment.service.PaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/suroegina/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /** Получение продуктов клиента (проксируется в сервис продуктов). */
    @GetMapping("/products")
    public List<ProductDto> getProducts(@RequestParam("userId") Long userId) {
        return paymentService.getProductsByUserId(userId);
    }

    /** Исполнение платежа: выбор продукта, проверка существования и средств, списание. */
    @PostMapping("/execute")
    public PaymentResponseDto executePayment(@RequestBody PaymentRequestDto request) {
        return paymentService.executePayment(request);
    }
}
