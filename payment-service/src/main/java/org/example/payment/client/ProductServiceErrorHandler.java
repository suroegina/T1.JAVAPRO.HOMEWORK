package org.example.payment.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.payment.exception.MyPaymentException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Обработчик ошибок RestTemplate: когда сервис продуктов отвечает 4xx/5xx,
 * пробрасываем ошибку наверх как MyPaymentException, сохраняя статус,
 * код и сообщение (сервис продуктов отдаёт тело вида {"code":..., "message":...}).
 */
public class ProductServiceErrorHandler implements ResponseErrorHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        int status = response.getStatusCode().value();

        String code = "PRODUCT_SERVICE_ERROR";
        String message = body;
        try {
            JsonNode node = objectMapper.readTree(body);
            if (node.has("code")) {
                code = node.get("code").asText();
            }
            if (node.has("message")) {
                message = node.get("message").asText();
            }
        } catch (Exception ignored) {
            // тело не JSON — оставляем как есть
        }
        throw new MyPaymentException(status, code, message);
    }
}
