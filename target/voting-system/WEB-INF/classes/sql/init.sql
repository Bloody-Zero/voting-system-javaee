-- ============================================================
-- SQL скрипт инициализации базы данных
-- Система голосования — PostgreSQL
-- ============================================================

-- Удаление таблиц (если существуют) для чистой инициализации
DROP TABLE IF EXISTS choice CASCADE;
DROP TABLE IF EXISTS question CASCADE;
DROP TABLE IF EXISTS vote CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================================
-- Таблица: users (Пользователи)
-- ============================================================
CREATE TABLE users (
    id         BIGSERIAL PRIMARY KEY,
    last_name  VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    phone      VARCHAR(20),
    status     VARCHAR(50)  NOT NULL DEFAULT 'NOT_VOTED',
    username   VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'USER'
);

-- ============================================================
-- Таблица: vote (Голосования)
-- ============================================================
CREATE TABLE vote (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    date_start  TIMESTAMP,
    date_finish TIMESTAMP,
    status      VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE'
);

-- ============================================================
-- Таблица: question (Вопросы голосования)
-- ============================================================
CREATE TABLE question (
    id        BIGSERIAL PRIMARY KEY,
    vote_id   BIGINT NOT NULL REFERENCES vote(id) ON DELETE CASCADE,
    content   TEXT   NOT NULL,
    date_vote TIMESTAMP
);

-- ============================================================
-- Таблица: choice (Результаты голосования)
-- ============================================================
CREATE TABLE choice (
    id          BIGSERIAL PRIMARY KEY,
    question_id BIGINT  NOT NULL REFERENCES question(id) ON DELETE CASCADE,
    user_id     BIGINT  NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    choice_user VARCHAR(255),
    CONSTRAINT unique_question_user UNIQUE (question_id, user_id)
);

-- ============================================================
-- Индексы для оптимизации запросов
-- ============================================================
CREATE INDEX idx_question_vote_id ON question(vote_id);
CREATE INDEX idx_choice_question_id ON choice(question_id);
CREATE INDEX idx_choice_user_id ON choice(user_id);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);

-- ============================================================
-- Тестовые данные
-- ============================================================

-- Пользователи
-- Пароль "admin123" хеширован через SHA-256:
-- SHA-256("admin123") = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- Пароль "user123" хеширован через SHA-256:
-- SHA-256("user123") = ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f

INSERT INTO users (last_name, first_name, email, phone, status, username, password, role) VALUES
('Админов', 'Админ', 'admin@voting.ru', '+79001234567', 'NOT_VOTED', 'admin',
 '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN'),
('Иванов', 'Иван', 'ivanov@mail.ru', '+79001112233', 'NOT_VOTED', 'ivanov',
 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'USER'),
('Петрова', 'Мария', 'petrova@mail.ru', '+79004445566', 'NOT_VOTED', 'petrova',
 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'USER'),
('Сидоров', 'Алексей', 'sidorov@mail.ru', '+79007778899', 'NOT_VOTED', 'sidorov',
 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'USER');

-- Голосования
INSERT INTO vote (title, date_start, date_finish, status) VALUES
('Выбор темы для корпоративного мероприятия',
 '2026-04-01 09:00:00', '2026-04-30 23:59:59', 'ACTIVE'),
('Оценка качества обслуживания в столовой',
 '2026-03-15 08:00:00', '2026-04-15 18:00:00', 'ACTIVE'),
('Выбор названия для нового продукта',
 '2026-02-01 10:00:00', '2026-03-01 10:00:00', 'COMPLETED');

-- Вопросы для голосования 1
INSERT INTO question (vote_id, content, date_vote) VALUES
(1, 'Какой формат мероприятия вы предпочитаете?', CURRENT_TIMESTAMP),
(1, 'Какое время суток вам подходит?', CURRENT_TIMESTAMP),
(1, 'Какой бюджет вы считаете оптимальным?', CURRENT_TIMESTAMP);

-- Вопросы для голосования 2
INSERT INTO question (vote_id, content, date_vote) VALUES
(2, 'Оцените качество еды (1-5)', CURRENT_TIMESTAMP),
(2, 'Оцените чистоту помещения (1-5)', CURRENT_TIMESTAMP),
(2, 'Оцените вежливость персонала (1-5)', CURRENT_TIMESTAMP);

-- Вопросы для голосования 3 (завершено)
INSERT INTO question (vote_id, content, date_vote) VALUES
(3, 'Какое название вам нравится больше?', CURRENT_TIMESTAMP),
(3, 'Какой слогон лучше подходит?', CURRENT_TIMESTAMP);

-- Результаты голосования (choices) — для завершённого голосования №3
INSERT INTO choice (question_id, user_id, choice_user) VALUES
(7, 2, 'Вариант А'),
(7, 3, 'Вариант Б'),
(7, 4, 'Вариант А'),
(8, 2, 'Слогон 1'),
(8, 3, 'Слогон 2'),
(8, 4, 'Слогон 1');

-- Обновим статус пользователей, проголосовавших в голосовании №3
UPDATE users SET status = 'VOTED' WHERE id IN (2, 3, 4);

-- ============================================================
-- Проверка: количество записей
-- ============================================================
SELECT 'users' AS table_name, COUNT(*) FROM users
UNION ALL
SELECT 'vote', COUNT(*) FROM vote
UNION ALL
SELECT 'question', COUNT(*) FROM question
UNION ALL
SELECT 'choice', COUNT(*) FROM choice;
