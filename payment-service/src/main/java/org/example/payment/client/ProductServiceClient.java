package org.example.payment.client;

import org.example.payment.dto.ProductDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * HTTP-клиент сервиса продуктов. Все вызовы идут через RestTemplate.
 */
@Component
public class ProductServiceClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public ProductServiceClient(RestTemplate restTemplate,
                                @Value("${product.service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    /** GET /v1/suroegina/products?userId={userId} — продукты клиента. */
    public List<ProductDto> getProductsByUserId(Long userId) {
        String url = baseUrl + "/v1/suroegina/products?userId=" + userId;
        ResponseEntity<List<ProductDto>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<ProductDto>>() {
                });
        return response.getBody();
    }

    /** GET /v1/suroegina/products/{id} — один продукт (проверка существования). */
    public ProductDto getProductById(Long id) {
        String url = baseUrl + "/v1/suroegina/products/" + id;
        return restTemplate.getForObject(url, ProductDto.class);
    }

    /** POST /v1/suroegina/products/{id}/debit — списание средств. */
    public ProductDto debit(Long id, BigDecimal amount) {
        String url = baseUrl + "/v1/suroegina/products/" + id + "/debit";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, BigDecimal> body = Map.of("amount", amount);
        HttpEntity<Map<String, BigDecimal>> entity = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(url, entity, ProductDto.class);
    }
}
