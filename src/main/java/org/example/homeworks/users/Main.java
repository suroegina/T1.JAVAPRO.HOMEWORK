package org.example.homeworks.users;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * Точка входа. Здесь мы:
 *   1) создаём Spring-контекст;
 *   2) достаём из него готовый бин UserService;
 *   3) дёргаем все операции, которые требовались в задании.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Старт домашнего задания 'users' ===");

        // try-with-resources закроет контекст в конце; заодно закроется
        // и пул соединений HikariCP (у бина dataSource указан destroyMethod = "close").
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            // Получаем готовый бин UserService — все его зависимости Sprin уже собрал за нас.
            UserService service = context.getBean(UserService.class);
            // DAO тоже есть в контексте — достаём его, чтобы показать операцию update.
            UserDao dao = context.getBean(UserDao.class);
            boolean deletedAll = service.deleteAll();
            System.out.println("Очистка таблицы users прошла успешно? " + deletedAll);
            System.out.println("\n--- 1. Создаём пользователей ---");
            User user1 = service.create("Daria");
            User user2 = service.create("Ivan");
            User user3 = service.create("George");
            User user4 = service.create("Diana");

            System.out.println("Создано: " + user1 + ", " + user2 + ", " + user3 + ", " + user4);

            System.out.println("\n--- 2. Получаем одного по id ---");
            System.out.println("Пользователь id=" + user1.getId() + ": " + service.getOne(user1.getId()));

            System.out.println("\n--- 3. Получаем всех ---");
            printAll(service);

            System.out.println("\n--- 4. Обновляем user1: Daria -> Daria_new (операция update у DAO) ---");
            int updated = dao.update(user1.getId(), "Daria_new");
            System.out.println("Затронуто строк: " + updated);
            System.out.println("Теперь: " + service.getOne(user1.getId()));

            System.out.println("\n--- 5. Удаляем user2: Ivan ---");
            boolean deleted = service.delete(user2.getId());
            System.out.println("Удаление Ivan прошло успешно? " + deleted);
            printAll(service);

            System.out.println("\n--- 6. Пытаемся получить удалённого Ivan ---");
            System.out.println("getOne(" + user2.getId() + ") = " + service.getOne(user2.getId()));

            System.out.println("\n--- 7. Пытаемся создать пользователя с пустым именем ---");
            System.out.println("create(\"  \") = " + service.create("  "));

            System.out.println("\n--- 8. Пытаемся создать дубликат имени (сработает UNIQUE) ---");
            try {
                service.create("George");
            } catch (Exception e) {
                System.out.println("Ожидаемая ошибка: " + e.getClass().getSimpleName()
                        + " (username='George' уже существует)");
            }

            System.out.println("\n=== Готово ===");
        }
    }

    private static void printAll(UserService service) {
        List<User> users = service.getAll();
        System.out.println("Всего пользователей: " + users.size());
        users.forEach(u -> System.out.println("  " + u));
    }
}

