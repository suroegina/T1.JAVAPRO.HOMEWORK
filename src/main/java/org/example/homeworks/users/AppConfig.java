package org.example.homeworks.users;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Конфигурация Spring.
 * Класс помечен @Configuration, значит Spring при старте вызовет все методы,
 * помеченные @Bean, и положит их результаты в контейнер (в "контекст").
 * Дальше Spring сам аккуратно передаст нужные бины в конструкторы других бинов:
 * dataSource -> userDao -> userService.
 */
@Configuration
@EnableTransactionManagement
public class AppConfig {

    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);

    /** 1. Пул соединений. Читаем параметры из application.properties. */
    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        Properties props = loadProperties();

        HikariConfig cfg = new HikariConfig();
        cfg.setJdbcUrl(props.getProperty("db.url"));
        cfg.setUsername(props.getProperty("db.username"));
        cfg.setPassword(props.getProperty("db.password"));
        cfg.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.size", "5")));
        cfg.setPoolName("users-hw-pool");

        log.info("Создаю пул соединений HikariCP -> {}", cfg.getJdbcUrl());
        return new HikariDataSource(cfg);
    }

    /**
     * Менеджер транзакций. Нужен, чтобы аннотации @Transactional в UserDao
     * реально работали: именно он открывает/коммитит/откатывает транзакцию.
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /** 2. DAO. Spring видит, что конструктору нужен DataSource, и подставит бин из dataSource(). */
    @Bean
    public UserDao userDao(DataSource dataSource) {
        return new UserDao(dataSource);
    }

    /** 3. Сервис. Аналогично Spring подставит сюда бин UserDao. */
    @Bean
    public UserService userService(UserDao userDao) {
        return new UserService(userDao);
    }

    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream in = AppConfig.class.getResourceAsStream("/application.properties")) {
            if (in == null) {
                throw new IllegalStateException(
                        "Не найден application.properties в classpath (src/main/resources)");
            }
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать application.properties", e);
        }
        return props;
    }
}

