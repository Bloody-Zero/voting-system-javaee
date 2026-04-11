package com.example.voting.service;

import com.example.voting.dao.ChoiceDAO;
import com.example.voting.dao.QuestionDAO;
import com.example.voting.dao.UserDAO;
import com.example.voting.entity.Choice;
import com.example.voting.entity.Question;
import com.example.voting.entity.User;
import com.example.voting.entity.Vote;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Тесты для ChoiceService.
 * Покрывают операции CRUD, проверку дубликатов, обновление статуса пользователя.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ChoiceService — тесты")
class ChoiceServiceTest {

    @Mock
    private ChoiceDAO choiceDAO;

    @Mock
    private QuestionDAO questionDAO;

    @Mock
    private UserDAO userDAO;

    private ChoiceService choiceService;

    @BeforeEach
    void setUp() {
        choiceService = new ChoiceService(choiceDAO, questionDAO, userDAO);
    }

    @Test
    @DisplayName("Получение всех выборов")
    void testGetAllChoices() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        question.setId(1L);
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "VOTED");
        user.setId(1L);

        Choice c1 = new Choice(question, user, "Вариант 1");
        c1.setId(1L);
        Choice c2 = new Choice(question, user, "Вариант 2");
        c2.setId(2L);

        when(choiceDAO.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Choice> choices = choiceService.getAllChoices();
        assertEquals(2, choices.size());
        assertEquals("Вариант 1", choices.get(0).getChoiceUser());

        verify(choiceDAO).findAll();
    }

    @Test
    @DisplayName("Получение выбора по ID — успешно")
    void testGetChoice_Success() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        question.setId(1L);
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "VOTED");
        user.setId(1L);
        Choice choice = new Choice(question, user, "Мой выбор");
        choice.setId(1L);

        when(choiceDAO.findById(1L)).thenReturn(choice);

        Choice found = choiceService.getChoice(1L);
        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("Мой выбор", found.getChoiceUser());

        verify(choiceDAO).findById(1L);
    }

    @Test
    @DisplayName("Получение выбора — null ID")
    void testGetChoice_NullId() {
        assertThrows(IllegalArgumentException.class, () -> choiceService.getChoice(null));
    }

    @Test
    @DisplayName("Получение выбора — не найден")
    void testGetChoice_NotFound() {
        when(choiceDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> choiceService.getChoice(999L));
    }

    @Test
    @DisplayName("Получение выборов по вопросу")
    void testGetChoicesByQuestion() {
        when(choiceDAO.findByQuestionId(1L)).thenReturn(Collections.emptyList());

        List<Choice> choices = choiceService.getChoicesByQuestion(1L);
        assertNotNull(choices);

        verify(choiceDAO).findByQuestionId(1L);
    }

    @Test
    @DisplayName("Получение выборов по вопросу — null ID")
    void testGetChoicesByQuestion_NullId() {
        assertThrows(IllegalArgumentException.class, () -> choiceService.getChoicesByQuestion(null));
    }

    @Test
    @DisplayName("Получение выборов по пользователю")
    void testGetChoicesByUser() {
        when(choiceDAO.findByUserId(1L)).thenReturn(Collections.emptyList());

        List<Choice> choices = choiceService.getChoicesByUser(1L);
        assertNotNull(choices);

        verify(choiceDAO).findByUserId(1L);
    }

    @Test
    @DisplayName("Получение выборов по пользователю — null ID")
    void testGetChoicesByUser_NullId() {
        assertThrows(IllegalArgumentException.class, () -> choiceService.getChoicesByUser(null));
    }

    @Test
    @DisplayName("Получение выбора по вопросу и пользователю")
    void testGetChoiceByQuestionAndUser() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        question.setId(1L);
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "VOTED");
        user.setId(1L);
        Choice choice = new Choice(question, user, "Выбор");
        choice.setId(1L);

        when(choiceDAO.findByQuestionAndUser(1L, 1L)).thenReturn(choice);

        Choice found = choiceService.getChoiceByQuestionAndUser(1L, 1L);
        assertNotNull(found);
        assertEquals("Выбор", found.getChoiceUser());

        verify(choiceDAO).findByQuestionAndUser(1L, 1L);
    }

    @Test
    @DisplayName("Сохранение выбора — успешно")
    void testSaveChoice_Success() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        question.setId(1L);
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "NOT_VOTED");
        user.setId(1L);
        Choice choice = new Choice(null, null, "Мой выбор");

        when(questionDAO.findById(1L)).thenReturn(question);
        when(userDAO.findById(1L)).thenReturn(user);
        when(choiceDAO.findByQuestionAndUser(1L, 1L)).thenReturn(null);

        choiceService.saveChoice(choice, 1L, 1L);

        verify(choiceDAO).save(choice);
        assertEquals(question, choice.getQuestion());
        assertEquals(user, choice.getUser());
    }

    @Test
    @DisplayName("Сохранение выбора — null выбор")
    void testSaveChoice_NullChoice() {
        assertThrows(IllegalArgumentException.class, () -> choiceService.saveChoice(null, 1L, 1L));
    }

    @Test
    @DisplayName("Сохранение выбора — null questionId")
    void testSaveChoice_NullQuestionId() {
        Choice choice = new Choice(null, null, "Выбор");
        assertThrows(IllegalArgumentException.class, () -> choiceService.saveChoice(choice, null, 1L));
    }

    @Test
    @DisplayName("Сохранение выбора — null userId")
    void testSaveChoice_NullUserId() {
        Choice choice = new Choice(null, null, "Выбор");
        assertThrows(IllegalArgumentException.class, () -> choiceService.saveChoice(choice, 1L, null));
    }

    @Test
    @DisplayName("Сохранение выбора — вопрос не найден")
    void testSaveChoice_QuestionNotFound() {
        Choice choice = new Choice(null, null, "Выбор");
        when(questionDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> choiceService.saveChoice(choice, 999L, 1L));
    }

    @Test
    @DisplayName("Сохранение выбора — пользователь не найден")
    void testSaveChoice_UserNotFound() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        question.setId(1L);
        Choice choice = new Choice(null, null, "Выбор");

        when(questionDAO.findById(1L)).thenReturn(question);
        when(userDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> choiceService.saveChoice(choice, 1L, 999L));
    }

    @Test
    @DisplayName("Сохранение выбора — пользователь уже голосовал")
    void testSaveChoice_AlreadyVoted() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        question.setId(1L);
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "VOTED");
        user.setId(1L);
        Choice existingChoice = new Choice(question, user, "Уже выбрал");
        Choice newChoice = new Choice(null, null, "Новый выбор");

        when(questionDAO.findById(1L)).thenReturn(question);
        when(userDAO.findById(1L)).thenReturn(user);
        when(choiceDAO.findByQuestionAndUser(1L, 1L)).thenReturn(existingChoice);

        assertThrows(IllegalArgumentException.class, () -> choiceService.saveChoice(newChoice, 1L, 1L));
    }

    @Test
    @DisplayName("Сохранение выбора — пустое значение выбора")
    void testSaveChoice_EmptyChoiceValue() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        question.setId(1L);
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "NOT_VOTED");
        user.setId(1L);
        Choice choice = new Choice(null, null, "");

        when(questionDAO.findById(1L)).thenReturn(question);
        when(userDAO.findById(1L)).thenReturn(user);
        when(choiceDAO.findByQuestionAndUser(1L, 1L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> choiceService.saveChoice(choice, 1L, 1L));
    }

    @Test
    @DisplayName("Удаление выбора — успешно")
    void testDeleteChoice_Success() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        question.setId(1L);
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "VOTED");
        user.setId(1L);
        Choice choice = new Choice(question, user, "Выбор");
        choice.setId(1L);

        when(choiceDAO.findById(1L)).thenReturn(choice);
        when(choiceDAO.findByUserId(1L)).thenReturn(Collections.emptyList());
        when(userDAO.findById(1L)).thenReturn(user);

        choiceService.deleteChoice(1L);

        verify(choiceDAO).delete(1L);
    }

    @Test
    @DisplayName("Удаление выбора — не найден")
    void testDeleteChoice_NotFound() {
        when(choiceDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> choiceService.deleteChoice(999L));
    }

    @Test
    @DisplayName("Подсчёт голосов за вопрос")
    void testGetVoteCountForQuestion() {
        when(choiceDAO.countByQuestion(1L)).thenReturn(5L);

        long count = choiceService.getVoteCountForQuestion(1L);
        assertEquals(5L, count);

        verify(choiceDAO).countByQuestion(1L);
    }

    @Test
    @DisplayName("Подсчёт голосов — null ID")
    void testGetVoteCountForQuestion_NullId() {
        assertThrows(IllegalArgumentException.class, () -> choiceService.getVoteCountForQuestion(null));
    }

    @Test
    @DisplayName("Обновление выбора — успешно")
    void testUpdateChoice_Success() {
        Vote vote = new Vote("Тест", LocalDateTime.now(), LocalDateTime.now().plusDays(7), "ACTIVE");
        vote.setId(1L);
        Question question = new Question(vote, "Вопрос", LocalDateTime.now());
        question.setId(1L);
        User user = new User("Иван", "Иванов", "ivan@test.ru", null, "VOTED");
        user.setId(1L);
        Choice existing = new Choice(question, user, "Старый выбор");
        existing.setId(1L);
        Choice updated = new Choice(question, user, "Новый выбор");
        updated.setId(1L);

        when(choiceDAO.findById(1L)).thenReturn(existing);

        choiceService.updateChoice(updated);

        verify(choiceDAO).save(updated);
    }

    @Test
    @DisplayName("Обновление выбора — null или без ID")
    void testUpdateChoice_NullId() {
        Choice choice = new Choice(null, null, "Выбор");
        assertThrows(IllegalArgumentException.class, () -> choiceService.updateChoice(choice));
    }

    @Test
    @DisplayName("Обновление выбора — не найден")
    void testUpdateChoice_NotFound() {
        Choice choice = new Choice(null, null, "Выбор");
        choice.setId(999L);
        when(choiceDAO.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> choiceService.updateChoice(choice));
    }
}
