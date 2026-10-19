package org.example.homeworks.users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {

    private final UserService userService;

    public AppRunner(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.printf("Очищаем таблицу USERS %n");
        User deleteAll = userService.deleteAll();


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

    }


}
