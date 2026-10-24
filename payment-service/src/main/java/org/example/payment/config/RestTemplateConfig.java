package org.example.payment.config;

import org.example.payment.client.ProductServiceErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурация HTTP-клиента для интеграции с сервисом продуктов.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        // Перехватываем ошибки сервиса продуктов (4xx/5xx) и превращаем их
        // в понятные исключения платёжного ядра.
        restTemplate.setErrorHandler(new ProductServiceErrorHandler());
        return restTemplate;
    }
}
