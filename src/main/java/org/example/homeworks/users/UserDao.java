package org.example.homeworks.users;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.List;

/**
 * DAO (Data Access Object) — слой, который умеет только одно: превращать
 * вызовы Java-методов в SQL-запросы к таблице users и обратно.
 * Здесь НЕТ бизнес-логики — только CRUD. Это называется "разделение
 * ответственности" (separation of concerns): если завтра таблица переименуется
 * или изменится её структура, править придётся только этот класс.
 */
public class UserDao {

    private final JdbcTemplate jdbc;

    public UserDao(DataSource dataSource) {
        this.jdbc = new JdbcTemplate(dataSource);
    }

    /**
     * CREATE — вставляет пользователя и возвращает его id, сгенерированный базой.
     */
    @Transactional
    public Long create(String username) {
        String sql = "INSERT INTO users (username) VALUES (?)";

        // KeyHolder нужен, чтобы получить значение колонки, сгенерированное СУБД
        // (у нас это bigserial PRIMARY KEY -> id).
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            // ВАЖНО: указываем колонки, которые считаем "сгенерированными".
            // Без этого PostgreSQL возвращает в ответе ВСЕ колонки строки,
            // и getKey() падает с ошибкой "current key entry contains multiple keys".
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, username);
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        return key == null ? null : key.longValue();
    }

    /**
     * READ — возвращает одного пользователя по id или null, если такого нет.
     */
    public User findById(Long id) {
        String sql = "SELECT id, username FROM users WHERE id = ?";
        List<User> list = jdbc.query(sql, USER_ROW_MAPPER, id);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * READ — возвращает всех пользователей.
     */
    public List<User> findAll() {
        String sql = "SELECT id, username FROM users ORDER BY id";
        return jdbc.query(sql, USER_ROW_MAPPER);
    }

    /**
     * UPDATE — меняет имя пользователя по id. Возвращает число затронутых строк.
     */
    @Transactional
    public int update(Long id, String newUsername) {
        String sql = "UPDATE users SET username = ? WHERE id = ?";
        return jdbc.update(sql, newUsername, id);
    }

    /**
     * DELETE — удаляет пользователя по id. Возвращает число удалённых строк.
     */
    @Transactional
    public int delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbc.update(sql, id);
    }
    /**
     * DELETE — удаляет всех пользователей.
     */
    @Transactional
    public int deleteAll() {
        String sql = "DELETE FROM users";
        return jdbc.update(sql);
    }
    /** Как преобразовать одну строку результата SELECT в объект User. */
    private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("username")
    );
}
