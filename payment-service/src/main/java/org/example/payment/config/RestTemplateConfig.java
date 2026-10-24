package org.example.payment.config;

import org.example.payment.client.ProductServiceErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

/**
 * Конфигурация HTTP-клиента для интеграции с сервисом продуктов.
 * Здесь сосредоточено всё «как ходить»: базовый URL и таймауты,
 * чтобы платёжное ядро не зависало на недоступном сервисе продуктов.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder,
                                     ProductServiceErrorHandler errorHandler,
                                     @Value("${product.service.url}") String baseUrl,
                                     @Value("${product.service.connect-timeout:5s}") Duration connectTimeout,
                                     @Value("${product.service.read-timeout:10s}") Duration readTimeout) {
        return builder
                .rootUri(baseUrl)                  // базовый URL сервиса продуктов
                .setConnectTimeout(connectTimeout)  // таймаут установки соединения
                .setReadTimeout(readTimeout)        // таймаут ожидания ответа
                .errorHandler(errorHandler)
                .build();
    }
}
