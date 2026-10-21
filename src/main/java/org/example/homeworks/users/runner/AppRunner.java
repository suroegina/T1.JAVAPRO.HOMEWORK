package org.example.homeworks.users.runner;

import org.example.homeworks.users.dto.ProductDto;
import org.example.homeworks.users.product.ProductType;
import org.example.homeworks.users.service.ProductService;
import org.example.homeworks.users.service.UserService;
import org.example.homeworks.users.user.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AppRunner implements CommandLineRunner {

    private final UserService userService;

    private final ProductService productService;

    public AppRunner(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.printf("Очищаем таблицу USERS и PRODUCTS %n");
        productService.deleteAll();
        userService.deleteAll();


        User firstUser = userService.createUser("Nikita");
        System.out.printf("Создан пользователь: %s%n", firstUser);

        User secondUser = userService.createUser("Diana");
        System.out.printf("Создан пользователь: %s%n", secondUser);

        User thirtyUser = userService.createUser("Ivan");
        System.out.printf("Создан пользователь: %s%n", thirtyUser);

        User retrieved = userService.getUserById(firstUser.getId());
        System.out.printf("Получен пользователь по id: %s%n", retrieved);

        userService.getAllUsers().forEach(System.out::println);

        userService.deleteUser(secondUser.getId());
        System.out.printf("Пользователь %s удалён.%n", secondUser.getUsername());

        System.out.println("После удаления:");
        userService.getAllUsers().forEach(System.out::println);

        System.out.println("\n--- Вызовы через query ---");
        userService.findByUsername("Nikita")
                .ifPresentOrElse(System.out::println, () -> System.out.println("Пользователь с именем 'Nikita' не найден"));

        System.out.println("Результат поиска несуществующего имени: " +
                userService.findByUsername("Unknown").map(User::toString).orElse("Unknown не найден"));

        ProductDto account1 = productService.createProduct(
                "4876243264710001",
                BigDecimal.valueOf(15000.50),
                ProductType.ACCOUNT,
                firstUser.getId()
        );
        ProductDto card1 = productService.createProduct(
                "4876243264710002",
                BigDecimal.valueOf(5000.00),
                ProductType.CARD,
                firstUser.getId()
        );
        ProductDto account2 = productService.createProduct(
                "4876243264710003",
                BigDecimal.ZERO,
                ProductType.ACCOUNT,
                thirtyUser.getId()
        );

        ProductDto account3 = productService.createProduct(
                "4876243264710004",
                BigDecimal.valueOf(1000.00),
                ProductType.ACCOUNT,
                thirtyUser.getId()
        );

        ProductDto account4 = productService.createProduct(
                "4876243264710005",
                BigDecimal.valueOf(100000.00),
                ProductType.CARD,
                thirtyUser.getId()
        );

        System.out.println("Продукты пользователя " + thirtyUser.getUsername() + ":");
        productService.getProductsByUserId(thirtyUser.getId())
                .forEach(System.out::println);
                ;

    }
}