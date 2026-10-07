-- Таблица пользователей ровно по заданию:
--   id       bigserial PRIMARY KEY  -> авто-инкремент (1, 2, 3, ...)
--   username varchar(255) UNIQUE    -> строка до 255 символов, значения уникальны
CREATE TABLE IF NOT EXISTS users (
    id       bigserial PRIMARY KEY,
    username varchar(255) UNIQUE
);

