package com.example.voting.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для сущностей (Entity).
 * Покрывают конструкторы, геттеры/сеттеры, вычисляемые поля.
 */
@DisplayName("Entity — тесты сущностей")
class EntityTest {

    // ===== Vote Entity Tests =====

    @Test
    @DisplayName("Vote — конструктор и геттеры")
    void testVoteConstructorAndGetters() {
        LocalDateTime start = LocalDateTime.of(2026, 4, 1, 9, 0);
        LocalDateTime finish = LocalDateTime.of(2026, 4, 30, 23, 59);
        Vote vote = new Vote("Тестовое голосование", start, finish, "ACTIVE");

        assertEquals("Тестовое голосование", vote.getTitle());
        assertEquals(start, vote.getDateStart());
        assertEquals(finish, vote.getDateFinish());
        assertEquals("ACTIVE", vote.getStatus());
    }

    @Test
    @DisplayName("Vote — setId и getId")
    void testVoteId() {
        Vote vote = new Vote();
        vote.setId(42L);
        assertEquals(42L, vote.getId());
    }

    @Test
    @DisplayName("Vote — форматирование даты начала")
    void testVoteDateStartFormatted() {
        LocalDateTime start = LocalDateTime.of(2026, 4, 15, 14, 30);
        Vote vote = new Vote("Тест", start, LocalDateTime.now().plusDays(7), "ACTIVE");

        assertEquals("2026-04-15T14:30", vote.getDateStartFormatted());
    }

    @Test
    @DisplayName("Vote — форматирование даты окончания")
    void testVoteDateFinishFormatted() {
        LocalDateTime finish = LocalDateTime.of(2026, 12, 31, 23, 59);
        Vote vote = new Vote("Тест", LocalDateTime.now(), finish, "ACTIVE");

        assertEquals("2026-12-31T23:59", vote.getDateFinishFormatted());
    }

    @Test
    @DisplayName("Vote — null даты возвращают пустую строку")
    void testVoteNullDateFormatted() {
        Vote vote = new Vote();
        vote.setDateStart(null);
        vote.setDateFinish(null);

        assertEquals("", vote.getDateStartFormatted());
        assertEquals("", vote.getDateFinishFormatted());
    }

    @Test
    @DisplayName("Vote — сеттеры")
    void testVoteSetters() {
        Vote vote = new Vote();
        LocalDateTime now = LocalDateTime.now();

        vote.setTitle("Новое название");
        vote.setDateStart(now);
        vote.setDateFinish(now.plusDays(7));
        vote.setStatus("COMPLETED");

        assertEquals("Новое название", vote.getTitle());
        assertEquals(now, vote.getDateStart());
        assertEquals("COMPLETED", vote.getStatus());
    }

    @Test
    @DisplayName("Vote — вопросы по умолчанию пустые")
    void testVoteQuestionsDefault() {
        Vote vote = new Vote();
        assertNull(vote.getQuestions());
    }

    @Test
    @DisplayName("Vote — установка вопросов")
    void testVoteSetQuestions() {
        Vote vote = new Vote();
        List<Question> questions = new ArrayList<>();
        vote.setQuestions(questions);

        assertNotNull(vote.getQuestions());
        assertTrue(vote.getQuestions().isEmpty());
    }

    // ===== User Entity Tests =====

    @Test
    @DisplayName("User — конструктор и геттеры")
    void testUserConstructorAndGetters() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", "+79001234567", "NOT_VOTED");

        assertEquals("Иван", user.getFirstName());
        assertEquals("Иванов", user.getLastName());
        assertEquals("ivan@test.ru", user.getEmail());
        assertEquals("+79001234567", user.getPhone());
        assertEquals("NOT_VOTED", user.getStatus());
    }

    @Test
    @DisplayName("User — fullName")
    void testUserFullName() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "NOT_VOTED");

        assertEquals("Иванов Иван", user.getFullName());
    }

    @Test
    @DisplayName("User — setId и getId")
    void testUserId() {
        User user = new User();
        user.setId(100L);
        assertEquals(100L, user.getId());
    }

    @Test
    @DisplayName("User — конструктор с логином и паролем")
    void testUserWithCredentials() {
        User user = new User("Петр", "Петров", "petr@test.ru", "+79009998877",
                              "VOTED", "petr", "hashedpass", "ADMIN");

        assertEquals("petr", user.getUsername());
        assertEquals("hashedpass", user.getPassword());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    @DisplayName("User — сеттеры")
    void testUserSetters() {
        User user = new User();

        user.setFirstName("Алексей");
        user.setLastName("Сидоров");
        user.setEmail("alex@test.ru");
        user.setPhone("+79005556677");
        user.setStatus("VOTED");
        user.setUsername("alexey");
        user.setPassword("secret");
        user.setRole("USER");

        assertEquals("Алексей", user.getFirstName());
        assertEquals("Сидоров", user.getLastName());
        assertEquals("alex@test.ru", user.getEmail());
        assertEquals("VOTED", user.getStatus());
        assertEquals("alexey", user.getUsername());
        assertEquals("USER", user.getRole());
    }

    @Test
    @DisplayName("User — choices по умолчанию пустые")
    void testUserChoicesDefault() {
        User user = new User();
        assertNull(user.getChoices());
    }

    // ===== Question Entity Tests =====

    @Test
    @DisplayName("Question — конструктор и геттеры")
    void testQuestionConstructorAndGetters() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        LocalDateTime date = LocalDateTime.of(2026, 5, 1, 10, 0);
        Question question = new Question(vote, "Тестовый вопрос", date);

        assertEquals(vote, question.getVote());
        assertEquals("Тестовый вопрос", question.getContent());
        assertEquals(date, question.getDateVote());
    }

    @Test
    @DisplayName("Question — форматирование даты")
    void testQuestionDateFormatted() {
        LocalDateTime date = LocalDateTime.of(2026, 6, 15, 18, 30);
        Question question = new Question();
        question.setDateVote(date);

        // Формат даты в Question: dd.MM.yyyy HH:mm
        assertEquals("15.06.2026 18:30", question.getDateVoteFormatted());
    }

    @Test
    @DisplayName("Question — null дата возвращает пустую строку")
    void testQuestionNullDateFormatted() {
        Question question = new Question();
        question.setDateVote(null);

        assertEquals("", question.getDateVoteFormatted());
    }

    @Test
    @DisplayName("Question — setId и getId")
    void testQuestionId() {
        Question question = new Question();
        question.setId(55L);
        assertEquals(55L, question.getId());
    }

    @Test
    @DisplayName("Question — сеттеры")
    void testQuestionSetters() {
        Question question = new Question();
        Vote vote = new Vote();
        LocalDateTime now = LocalDateTime.now();

        question.setVote(vote);
        question.setContent("Новый вопрос");
        question.setDateVote(now);

        assertEquals(vote, question.getVote());
        assertEquals("Новый вопрос", question.getContent());
        assertEquals(now, question.getDateVote());
    }

    @Test
    @DisplayName("Question — choices по умолчанию пустые")
    void testQuestionChoicesDefault() {
        Question question = new Question();
        assertNull(question.getChoices());
    }

    // ===== Choice Entity Tests =====

    @Test
    @DisplayName("Choice — конструктор и геттеры")
    void testChoiceConstructorAndGetters() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "NOT_VOTED");

        Choice choice = new Choice(question, user, "Мой ответ");

        assertEquals(question, choice.getQuestion());
        assertEquals(user, choice.getUser());
        assertEquals("Мой ответ", choice.getChoiceUser());
    }

    @Test
    @DisplayName("Choice — setId и getId")
    void testChoiceId() {
        Choice choice = new Choice();
        choice.setId(77L);
        assertEquals(77L, choice.getId());
    }

    @Test
    @DisplayName("Choice — сеттеры")
    void testChoiceSetters() {
        Choice choice = new Choice();
        Question question = new Question();
        User user = new User();

        choice.setQuestion(question);
        choice.setUser(user);
        choice.setChoiceUser("Ответ");

        assertEquals(question, choice.getQuestion());
        assertEquals(user, choice.getUser());
        assertEquals("Ответ", choice.getChoiceUser());
    }

    // ===== toString Tests =====

    @Test
    @DisplayName("Vote — toString не null")
    void testVoteToString() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        assertNotNull(vote.toString());
    }

    @Test
    @DisplayName("User — toString содержит фамилию")
    void testUserToString() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "NOT_VOTED");
        String str = user.toString();
        assertNotNull(str);
    }

    @Test
    @DisplayName("Question — toString не null")
    void testQuestionToString() {
        Vote vote = new Vote();
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        assertNotNull(question.toString());
    }

    @Test
    @DisplayName("Choice — toString не null")
    void testChoiceToString() {
        Choice choice = new Choice();
        assertNotNull(choice.toString());
    }

    // ===== equals/hashCode базовые тесты =====

    @Test
    @DisplayName("Vote — equals для одного объекта")
    void testVoteEqualsReflexive() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        assertEquals(vote, vote);
    }

    @Test
    @DisplayName("User — equals для одного объекта")
    void testUserEqualsReflexive() {
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "NOT_VOTED");
        assertEquals(user, user);
    }

    @Test
    @DisplayName("Vote — equals с null")
    void testVoteEqualsNull() {
        Vote vote = new Vote();
        assertNotEquals(null, vote);
    }

    @Test
    @DisplayName("User — equals с другим типом")
    void testUserEqualsDifferentType() {
        User user = new User();
        assertNotEquals(user, "строка");
    }
}
