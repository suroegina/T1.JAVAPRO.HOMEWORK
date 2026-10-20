package org.example.homeworks.users.service;

import org.example.homeworks.users.dto.UserDto;
import org.example.homeworks.users.repository.UserRepository;
import org.example.homeworks.users.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис — слой бизнес-логики. Прячет от "внешнего мира" детали работы с DAO
 * и добавляет проверки/поведение поверх "сырых" операций базы.
 * Задание: создавать, удалять, получать одного, получать всех.
 * Методы create() и delete() возвращают boolean — "получилось или нет"
 */
@Service
public class UserService {


    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private UserDto userDto(User user) {
        return new UserDto(user.getId(), user.getUsername());
    }
    public List<UserDto> getAllUsersDto() {
        return userRepository.findAll().stream()
                .map(this::userDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getUserByIdDto(Long id) {
        return userRepository.findById(id)
                .map(this::userDto);
    }

    /**
     * Создаёт пользователя.
     *
     * @return созданного пользователя (уже с присвоенным id) или null, если имя пустое.
     */
    @Transactional
    public User createUser(String username) {
        User user = new User(username);
        return userRepository.save(user);
    }

    /**
     * Удаляет пользователя по id.
     * <p>
     * return true, если строка действительно была удалена.
     */
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Пользователь с id=" + id + " не найден");
        }
        userRepository.deleteById(id);
    }
    /**
     * Удаляет всех пользователей
     *
     * @return true, если все строки действительно были удалены.
     */
    @Transactional
    public User deleteAll() {
        userRepository.deleteAllInBatch();
        return null;
    }
    /**
     * Возвращает одного пользователя по id (или null, если не найден).
     */
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Пользователь с id=" + id + " не найден"));
    }
    /**
     * Возвращает всех пользователей.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}