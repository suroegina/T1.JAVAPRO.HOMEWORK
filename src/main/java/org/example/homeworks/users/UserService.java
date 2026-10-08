package org.example.homeworks.users;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Сервис — слой бизнес-логики. Прячет от "внешнего мира" детали работы с DAO
 * и добавляет проверки/поведение поверх "сырых" операций базы.
 * Задание: создавать, удалять, получать одного, получать всех.
 * Методы create() и delete() возвращают boolean — "получилось или нет"
 */
@Component
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Создаёт пользователя.
     *
     * @return созданного пользователя (уже с присвоенным id) или null, если имя пустое.
     */
    public User create(String username) {
        if (username == null || username.isBlank()) {
            return null;
        }
        Long id = userDao.create(username.trim());
        return new User(id, username.trim());
    }

    /**
     * Удаляет пользователя по id.
     *
     * @return true, если строка действительно была удалена.
     */
    public boolean delete(Long id) {
        return userDao.delete(id) > 0;
    }

    /**
     * Удаляет пользователя всех пользователей
     *
     * @return true, если все строки действительно были удалены.
     */
    public boolean deleteAll() {
        return userDao.deleteAll() > 0;
    }

    /**
     * Возвращает одного пользователя по id (или null, если не найден).
     */
    public User getOne(Long id) {
        return userDao.findById(id);
    }

    /**
     * Возвращает всех пользователей.
     */
    public List<User> getAll() {
        return userDao.findAll();
    }
}

