package org.example.homeworks.users;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;


/**
 * Конфигурация Spring.
 * Класс помечен @Configuration, значит Spring при старте вызовет все методы,
 * помеченные @Bean, и положит их результаты в контейнер (в "контекст").
 * Дальше Spring сам аккуратно передаст нужные бины в конструкторы других бинов:
 * dataSource -> userDao -> userService.
 */
@Configuration
@Component
@ComponentScan
@PropertySource("classpath:application.properties")
public class AppConfig {
    @Value("${db.url}")
    private String jdbcUrl;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${db.pool.size}")
    private int poolSize;
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    /** 1. Пул соединений. Читаем параметры из application.properties. */
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(poolSize);

        log.info("Создаю пул соединений HikariCP -> {}", config.getJdbcUrl());
        return new HikariDataSource(config);
    }

}

